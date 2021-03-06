package GUI.Dialogs.Projects;

import Database.ProjectsModification;
import Database.TeamsModification;
import Entities.Project;
import Entities.Team;
import GUI.Dialogs.AbstractDialog;
import GUI.Dialogs.ExceptionAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AddProjectDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Button clearDate = new Button("Clear");

    public AddProjectDialog(){
        super();
        this.setTitle("New project");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField nameTF = new TextField();
        nameTF.setPromptText("Name");
        DatePicker beginDateDP = new DatePicker();
        beginDateDP.setValue(LocalDate.now());
        DatePicker endDateDP = new DatePicker();
        endDateDP.setPromptText("End date");

        ComboBox<Team> teamComboBox = new ComboBox<>();
        try {
            ObservableList<Team> teamObservableList = FXCollections.observableArrayList(new TeamsModification().importObject());
            teamComboBox.setItems(teamObservableList);
            teamComboBox.setEditable(false);

            teamComboBox.setConverter(new StringConverter<Team>() {

                @Override
                public String toString(Team object) {
                    return object.getName();
                }

                @Override
                public Team fromString(String string) {
                    return teamComboBox.getItems().stream().filter(ap ->
                            ap.getName().equals(string)).findFirst().orElse(null);
                }
            });
        } catch (SQLException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }

        Node loginButton = this.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);
        loginButton.disableProperty().bind(
                nameTF.textProperty().isEmpty()
                        .or( beginDateDP.valueProperty().isNull() )
                        .or( teamComboBox.valueProperty().isNull() )
        );

        clearDate.setOnMouseClicked((MouseEvent event) -> {
            endDateDP.setValue(null);
        });

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameTF, 1, 1);
        grid.add(new Label("Begin date:"), 0, 2);
        grid.add(beginDateDP, 1, 2);
        grid.add(new Label("End date:"), 0, 3);
        grid.add(endDateDP, 1, 3);
        grid.add(clearDate,2,3);
        grid.add(new Label("Team:"),0,4);
        grid.add(teamComboBox, 1, 4);

        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), beginDateDP.getValue(), endDateDP.getValue(), teamComboBox.getValue().getId());
            }
            return null;
        });
    }

    public Project popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Project project = new Project();
            project.setName(result.get().getName());
            project.setBeginDate(result.get().getBeginDate());
            project.setEndDate(result.get().getEndDate());
            project.setTeamId(result.get().getTeamId());
            try {
                ProjectsModification.addObject(project);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                return null;
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with adding new project", "Project already in database.").showAndWait();
                return null;
            }
            return project;
        }
        return null;
    }

    private class Result {
        private String name;
        private Date beginDate;
        private Date endDate = null;
        private Integer teamId;

        public Result(String name, LocalDate beginDate, LocalDate endDate, Integer teamId) {
            this.name = name;
            this.beginDate = Date.valueOf(beginDate);
            if(endDate != null){
                this.endDate = Date.valueOf(endDate);
            }
            this.teamId = teamId;
        }

        public String getName() {
            return name;
        }

        public Date getBeginDate() {
            return beginDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public Integer getTeamId() {
            return teamId;
        }
    }
}

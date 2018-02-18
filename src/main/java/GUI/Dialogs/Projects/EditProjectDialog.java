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
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class EditProjectDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Project projectAfterEdition;
    private Project projectBeforeEdition;

    public EditProjectDialog(Project project){
        super();
        projectBeforeEdition = project;
        projectAfterEdition = new Project(project);
        this.setTitle("Edit project");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField nameTF = new TextField();
        nameTF.setText(project.getName());
        DatePicker beginDateDP = new DatePicker();
        beginDateDP.setValue(project.getBeginDate().toLocalDate());
        DatePicker endDateDP = new DatePicker();
        if(project.getEndDate() == null) {
            endDateDP.setPromptText("End date");
        } else {
            endDateDP.setValue(project.getEndDate().toLocalDate());
        }

        ComboBox<Team> teamComboBox = new ComboBox<>();
        try {
            ObservableList<Team> teamObservableList = FXCollections.observableArrayList(new TeamsModification().importObject());
            teamComboBox.setItems(teamObservableList);
            teamComboBox.setEditable(false);
            for (int index = 0; index < teamObservableList.size(); index++) {
                if(teamObservableList.get(index).getName().equals(project.getTeamName())){
                    teamComboBox.getSelectionModel().select(index);
                    break;
                }
            }

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


        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameTF, 1, 1);
        grid.add(new Label("Begin date:"), 0, 2);
        grid.add(beginDateDP, 1, 2);
        grid.add(new Label("End date:"), 0, 3);
        grid.add(endDateDP, 1, 3);
        grid.add(new Label("Team:"),0,4);
        grid.add(teamComboBox, 1, 4);

        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), beginDateDP.getValue(), endDateDP.getValue(), teamComboBox.getValue().getName());
            }
            return null;
        });
    }

    public Project popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            projectAfterEdition.setName(result.get().getName());
            projectAfterEdition.setBeginDate(result.get().getBeginDate());
            projectAfterEdition.setEndDate(result.get().getEndDate());
            projectAfterEdition.setTeamName(result.get().getTeamName());
            try {
                ProjectsModification.editObject(projectBeforeEdition,projectAfterEdition);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with editing the project", "Project no longer in database.").showAndWait();
            }
            return projectAfterEdition;
        }
        return null;
    }

    private class Result {
        private String name;
        private Date beginDate;
        private Date endDate = null;
        private String teamName;

        public Result(String name, LocalDate beginDate, LocalDate endDate, String teamName) {
            this.name = name;
            this.beginDate = Date.valueOf(beginDate);
            if(endDate != null){
                this.endDate = Date.valueOf(endDate);
            }
            this.teamName = teamName;
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

        public String getTeamName() {
            return teamName;
        }
    }
}

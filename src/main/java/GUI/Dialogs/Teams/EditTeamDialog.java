package GUI.Dialogs.Teams;

import Database.TeamsModification;
import Database.WorkersModification;
import Entities.Team;
import Entities.Worker;
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

public class EditTeamDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Team teamAfterEdition;
    private Team teamBeforeEdition;

    public EditTeamDialog(Team team){
        super();
        teamBeforeEdition = team;
        teamAfterEdition = new Team(team);
        this.setTitle("Edit team");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameTF = new TextField();
        nameTF.setText(team.getName());
        DatePicker creationDateDP = new DatePicker();
        creationDateDP.setValue(team.getCreationDate().toLocalDate());

        ComboBox<Worker> leaderComboBox = new ComboBox<>();
        try {
            ObservableList<Worker> leaderObservableList = FXCollections.observableArrayList(new WorkersModification().importObject());
            leaderComboBox.setItems(leaderObservableList);
            leaderComboBox.setEditable(false);
            for (int index = 0; index < leaderObservableList.size(); index++) {
                if(leaderObservableList.get(index).getPesel().equals(team.getLeaderPesel())){
                    leaderComboBox.getSelectionModel().select(index);
                    break;
                }
            }

            leaderComboBox.setConverter(new StringConverter<Worker>() {

                @Override
                public String toString(Worker object) {
                    return object.getName() + " " + object.getLastName() + " " + object.getPesel();
                }

                @Override
                public Worker fromString(String string) {
                    return leaderComboBox.getItems().stream().filter(ap ->
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
        );


        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameTF, 1, 1);
        grid.add(new Label("Creation date:"), 0, 2);
        grid.add(creationDateDP, 1, 2);
        grid.add(new Label("Leader:"), 0, 3);
        grid.add(leaderComboBox, 1, 3);

        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), creationDateDP.getValue(), leaderComboBox.getValue());
            }
            return null;
        });
    }

    public Team popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            teamAfterEdition.setName(result.get().getName());
            teamAfterEdition.setCreationDate(result.get().getCreationDate());
            teamAfterEdition.setLeaderPesel(result.get().getPeselLeader());
            try {
                TeamsModification.editObject(teamBeforeEdition,teamAfterEdition);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with editing the team", "Team no longer in database.").showAndWait();
            }
            return teamAfterEdition;
        }
        return null;
    }

    private class Result {
        private String name;
        private Date creationDate;
        private String peselLeader = null;

        public Result(String name, LocalDate creationDate, Worker leader) {
            this.name = name;
            this.creationDate = Date.valueOf(creationDate);
            if(leader != null) {
                this.peselLeader = leader.getPesel();
            }
        }

        public String getName() {
            return name;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public String getPeselLeader() {
            return peselLeader;
        }
    }
}

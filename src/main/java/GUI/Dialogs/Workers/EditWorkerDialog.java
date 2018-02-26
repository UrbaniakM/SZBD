package GUI.Dialogs.Workers;

import Database.PositionsModification;
import Database.TeamsModification;
import Database.WorkersModification;
import Entities.Position;
import Entities.Team;
import Entities.Worker;
import GUI.Dialogs.AbstractDialog;
import GUI.Dialogs.ExceptionAlert;
import GUI.Overviews.Positions;
import GUI.Overviews.Teams;
import GUI.TextFieldRestrictions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;


public class EditWorkerDialog extends AbstractDialog {
    ButtonType confirmButtonType;
    Worker workerAfterEdition;
    Worker workerBeforeEdition;

    public EditWorkerDialog(Worker worker){
        super();
        workerAfterEdition = worker;
        workerBeforeEdition = new Worker(worker);

        this.setTitle("Edit worker");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameTF = new TextField();
        nameTF.setText(workerBeforeEdition.getName());
        TextField lastNameTF = new TextField();
        lastNameTF.setText(workerBeforeEdition.getLastName());
        TextField peselTF = new TextField();
        peselTF.setText(workerBeforeEdition.getPesel());
        DatePicker hireDateDP = new DatePicker();
        hireDateDP.setValue(workerBeforeEdition.getHireDate().toLocalDate());
        TextField bonusTF = new TextField();
        bonusTF.setPromptText("Bonus");
        bonusTF.setText(workerBeforeEdition.getBonus().toString());

        ComboBox<Position> positionComboBox = new ComboBox<>();
            ObservableList<Position> positionObservableList = FXCollections.observableArrayList(Positions.positionsObservableList);
            positionComboBox.setItems(positionObservableList);
            positionComboBox.setEditable(false);
            for (int index = 0; index < positionObservableList.size(); index++) {
                if(positionObservableList.get(index).getId().equals(worker.getPositionId())){
                    positionComboBox.getSelectionModel().select(index);
                    break;
                }
            }

            positionComboBox.setConverter(new StringConverter<Position>() {

                @Override
                public String toString(Position object) {
                    return object.getName() + " " + object.getWage();
                }

                @Override
                public Position fromString(String string) {
                    return positionComboBox.getItems().stream().filter(ap ->
                            ap.getName().equals(string)).findFirst().orElse(null);
                }
            });

        ComboBox<Team> teamComboBox = new ComboBox<>();
            ObservableList<Team> teamObservableList = FXCollections.observableArrayList(Teams.teamsObservableList);
            teamComboBox.setItems(teamObservableList);
            teamComboBox.setEditable(false);
            for (int index = 0; index < teamObservableList.size(); index++) {
                if(teamObservableList.get(index).getId().equals(worker.getTeamId())){
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

        Node loginButton = this.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);
        loginButton.disableProperty().bind(
                nameTF.textProperty().isEmpty()
                        .or( positionComboBox.valueProperty().isNull() )
                        .or( lastNameTF.textProperty().isEmpty() )
                        .or( peselTF.textProperty().isEmpty() )
        );

        TextFieldRestrictions.addIntegerRestriction(bonusTF);
        TextFieldRestrictions.addIntegerRestriction(peselTF);

        TextFieldRestrictions.addTextLimiter(nameTF,32);
        TextFieldRestrictions.addTextLimiter(lastNameTF,32);
        TextFieldRestrictions.addTextLimiter(peselTF,11);
        TextFieldRestrictions.addTextLimiter(bonusTF,6);

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameTF, 1, 1);
        grid.add(new Label("Last name:"), 0, 2);
        grid.add(lastNameTF, 1, 2);
        grid.add(new Label("Pesel:"), 0, 3);
        grid.add(peselTF, 1, 3);
        grid.add(new Label("Hire Date:"), 0, 4);
        grid.add(hireDateDP, 1, 4);
        grid.add(new Label("Bonus:"), 0, 5);
        grid.add(bonusTF, 1, 5);
        grid.add(new Label("Position:"),0,6);
        grid.add(positionComboBox, 1, 6);
        grid.add(new Label("Team:"),0,7);
        grid.add(teamComboBox, 1, 7);


        this.getDialogPane().setContent(grid);
        Platform.runLater(() -> nameTF.requestFocus());
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), lastNameTF.getText(), peselTF.getText(), hireDateDP.getValue(), bonusTF.getText(),
                        positionComboBox.getValue().getId(), teamComboBox.getValue());
            }
            return null;
        });
    }
    public Worker popDialog(){
        Optional<Result> result = this.showAndWait();

        if (result.isPresent()) {
            workerAfterEdition.setName(result.get().getName());
            workerAfterEdition.setLastName(result.get().getLastName());
            workerAfterEdition.setPesel(result.get().getPesel());
            workerAfterEdition.setHireDate(result.get().getHireDate());
            workerAfterEdition.setBonus(result.get().getBonus());
            workerAfterEdition.setId(workerBeforeEdition.getId());
            workerAfterEdition.setTeamId(result.get().getTeamId());
            workerAfterEdition.setPositionId(result.get().getPositionId());
            try{
                WorkersModification.editObject(workerBeforeEdition,workerAfterEdition);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                return null;
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with editing the worker", "Worker no longer in database.").showAndWait();
                return null;
            }
            return workerAfterEdition;
        }
        return null;
    }


    private class Result {
        private String name;
        private String lastName;
        private String pesel;
        private Date hireDate;
        private Integer bonus = null;
        private Integer positionId;
        private Integer teamId = null;

        public Result(String name, String lastName, String pesel, LocalDate hireDate, String bonus, Integer positionId, Team team){
            this.name = name;
            this.lastName = lastName;
            this.pesel = pesel;
            this.hireDate = Date.valueOf(hireDate);
            if(bonus != null && !bonus.trim().equals("")) {
                this.bonus = Integer.valueOf(bonus);
            }
            this.positionId = positionId;
            if(team != null){
                this.teamId = team.getId();
            }
        }

        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPesel() {
            return pesel;
        }

        public Date getHireDate() {
            return hireDate;
        }

        public Integer getBonus() {
            return bonus;
        }

        public Integer getPositionId() {
            return positionId;
        }

        public Integer getTeamId() {
            return teamId;
        }
    }
}

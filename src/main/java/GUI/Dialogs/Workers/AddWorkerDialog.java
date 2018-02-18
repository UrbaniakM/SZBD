package GUI.Dialogs.Workers;

import Database.PositionsModification;
import Database.TeamsModification;
import Database.WorkersModification;
import Entities.Position;
import Entities.Team;
import Entities.Worker;
import GUI.Dialogs.AbstractDialog;
import GUI.Dialogs.ExceptionAlert;
import GUI.TextFieldRestrictions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;


public class AddWorkerDialog extends AbstractDialog {
    ButtonType confirmButtonType;

    public AddWorkerDialog(){
        super();
        this.setTitle("New worker");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameTF = new TextField();
        nameTF.setPromptText("Name");
        TextField lastNameTF = new TextField();
        lastNameTF.setPromptText("Last name");
        TextField peselTF = new TextField();
        peselTF.setPromptText("Pesel");
        DatePicker hireDateDP = new DatePicker();
        hireDateDP.setValue(LocalDate.now());
        TextField bonusTF = new TextField();
        bonusTF.setPromptText("Bonus");

        ComboBox<Position> positionComboBox = new ComboBox<>();
        try {
            ObservableList<Position> positionObservableList = FXCollections.observableArrayList(new PositionsModification().importObject());
            positionComboBox.setItems(positionObservableList);
            positionComboBox.setEditable(false);

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
        } catch (SQLException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }

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


        TextFieldRestrictions.addIntegerRestriction(bonusTF);
        TextFieldRestrictions.addIntegerRestriction(peselTF);

        TextFieldRestrictions.addTextLimiter(nameTF,32);
        TextFieldRestrictions.addTextLimiter(lastNameTF,32);
        TextFieldRestrictions.addTextLimiter(peselTF,11);
        TextFieldRestrictions.addTextLimiter(bonusTF,6);

        Node loginButton = this.getDialogPane().lookupButton(confirmButtonType); // TODO: copy to another dialogs
        loginButton.setDisable(true);
        loginButton.disableProperty().bind(
                nameTF.textProperty().isEmpty()
                        .or( positionComboBox.valueProperty().isNull() )
                        .or( lastNameTF.textProperty().isEmpty() )
                        .or( peselTF.textProperty().isEmpty() )
        );


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
                        positionComboBox.getValue().getName(), teamComboBox.getValue());
            }
            return null;
        });
    }
    public Worker popDialog() {
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Worker worker = new Worker();
            worker.setName(result.get().getName());
            worker.setLastName(result.get().getLastName());
            worker.setPesel(result.get().getPesel());
            worker.setHireDate(result.get().getHireDate());
            worker.setBonus(result.get().getBonus());
            worker.setPositionName(result.get().getPositionName());
            worker.setTeamName(result.get().getTeamName());
            try{
                WorkersModification.addObject(worker);
            } catch (SQLException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with adding new worker", "Worker with this PESEL already in database.").showAndWait();
            }
            return worker;
        }
        return null;
    }


    private class Result {
        private String name;
        private String lastName;
        private String pesel;
        private Date hireDate;
        private Integer bonus = null;
        private String positionName;
        private String teamName = null;

        public Result(String name, String lastName, String pesel, LocalDate hireDate, String bonus, String positionName, Team team){
            this.name = name;
            this.lastName = lastName;
            this.pesel = pesel;
            this.hireDate = Date.valueOf(hireDate);
            if(bonus != null && !bonus.trim().equals("")) {
                this.bonus = Integer.valueOf(bonus);
            }
            this.positionName = positionName;
            if(team != null){  // TODO: allow team and bonus to be null - W KAZDYM DIALOGU
                this.teamName = team.getName();
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

        public String getPositionName() {
            return positionName;
        }

        public String getTeamName() {
            return teamName;
        }
    }
}

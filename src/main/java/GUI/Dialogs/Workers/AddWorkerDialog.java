package GUI.Dialogs.Workers;

import Database.PositionsModification;
import Database.WorkersModification;
import Entities.Position;
import Entities.Worker;
import GUI.Dialogs.AbstractDialog;
import GUI.TextFieldRestrictions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;


//public class AddWorkerDialog extends Dialog implements AbstractDialog {
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
        hireDateDP.setPromptText("Hire date");
        TextField bonusTF = new TextField();
        bonusTF.setPromptText("Bonus");

        ComboBox<Position> positionComboBox = new ComboBox<>();
        ObservableList<Position> observableList = FXCollections.observableArrayList(new PositionsModification().importObject());
        positionComboBox.setItems(observableList);
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


        // TODO: position, team

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

        this.getDialogPane().setContent(grid);
        Platform.runLater(() -> nameTF.requestFocus());
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), lastNameTF.getText(), peselTF.getText(), hireDateDP.getValue(), bonusTF.getText(),
                        positionComboBox.getValue().getName());
            }
            return null;
        });
    }
    public Worker popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Worker worker = new Worker();
            worker.setName(result.get().getName());
            worker.setLastName(result.get().getLastName());
            worker.setPesel(result.get().getPesel());
            worker.setHireDate(result.get().getHireDate());
            worker.setBonus(result.get().getBonus());
            worker.setPositionName(result.get().getPositionName());
            WorkersModification.addObject(worker);
            return worker;
        }
        return null;
    }


    private class Result {
        private String name;
        private String lastName;
        private String pesel;
        private Date hireDate;
        private Integer bonus;
        private String positionName;

        public Result(String name, String lastName, String pesel, LocalDate hireDate, String bonus, String positionName){
            this.name = name;
            this.lastName = lastName;
            this.pesel = pesel;
            this.hireDate = Date.valueOf(hireDate);
            this.bonus = Integer.valueOf(bonus);
            this.positionName = positionName;
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
    }
}

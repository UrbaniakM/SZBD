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

import java.sql.Connection;
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

        /*ComboBox<Position> positionComboBox = new ComboBox<>();
        ObservableList<Position> observableList = FXCollections.observableArrayList(new PositionsModification().importObject());
        positionComboBox.setItems();*/

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

        this.getDialogPane().setContent(grid);
        Platform.runLater(() -> nameTF.requestFocus());
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), lastNameTF.getText(), peselTF.getText(), hireDateDP.getValue(), bonusTF.getText());
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
            WorkersModification.addObject(worker);
            return worker;
        }
        return null;
    }


    private class Result {
        String name;
        String lastName;
        String pesel;
        Date hireDate;
        Integer bonus;

        public Result(String name, String lastName, String pesel, LocalDate hireDate, String bonus){
            this.name = name;
            this.lastName = lastName;
            this.pesel = pesel;
            this.hireDate = Date.valueOf(hireDate);
            this.bonus = Integer.valueOf(bonus);
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
    }
}

package GUI.Dialogs.Workers;

import Entities.Worker;
import Database.WorkersModification;
import GUI.Dialogs.AbstractDialog;
import GUI.TextFieldRestrictions;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javax.xml.soap.Text;
import java.sql.Connection;
import java.sql.Date;
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
        hireDateDP.setPromptText("Hire date");
        TextField hoursPerWeekTF = new TextField();
        hoursPerWeekTF.setPromptText("Working hours per week");
        TextField wageTF = new TextField();
        wageTF.setPromptText("Wage per hour");

        TextFieldRestrictions.addIntegerRestriction(hoursPerWeekTF);
        TextFieldRestrictions.addIntegerRestriction(wageTF);
        TextFieldRestrictions.addIntegerRestriction(peselTF);

        TextFieldRestrictions.addTextLimiter(nameTF,32);
        TextFieldRestrictions.addTextLimiter(lastNameTF,32);
        TextFieldRestrictions.addTextLimiter(peselTF,11);
        TextFieldRestrictions.addTextLimiter(hoursPerWeekTF,2);
        TextFieldRestrictions.addTextLimiter(wageTF,6);

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameTF, 1, 1);
        grid.add(new Label("Last name:"), 0, 2);
        grid.add(lastNameTF, 1, 2);
        grid.add(new Label("Pesel:"), 0, 3);
        grid.add(peselTF, 1, 3);
        grid.add(new Label("Hire Date:"), 0, 4);
        grid.add(hireDateDP, 1, 4);
        grid.add(new Label("Working hours per week:"), 0, 5);
        grid.add(hoursPerWeekTF, 1, 5);
        grid.add(new Label("Wage:"), 0, 6);
        grid.add(wageTF, 1, 6);

        this.getDialogPane().setContent(grid);
        Platform.runLater(() -> nameTF.requestFocus());
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), lastNameTF.getText(), peselTF.getText(), hireDateDP.getValue(), hoursPerWeekTF.getText(), wageTF.getText());
            }
            return null;
        });
    }
    public void popDialog(Connection connection){
        Optional<Result> result = this.showAndWait();

        if (result.isPresent()) {
            Worker worker = new Worker();
            worker.setName(result.get().getName());
            worker.setLastName(result.get().getLastName());
            worker.setPesel(result.get().getPesel());
            worker.setHireDate(result.get().getHireDate());
            worker.setHoursPerWeek(result.get().getHoursPerWeek());
            worker.setWage(result.get().getWage());
            WorkersModification.addWorker(worker, connection);
        }

    }


    private class Result {
        String name;
        String lastName;
        String pesel;
        Date hireDate;
        Integer hoursPerWeek;
        Integer wage;

        public Result(String name, String lastName, String pesel, LocalDate hireDate, String hoursPerWeek, String wage){
            this.name = name;
            this.lastName = lastName;
            this.pesel = pesel;
            this.hireDate = Date.valueOf(hireDate);
            this.hoursPerWeek = Integer.valueOf(hoursPerWeek);
            this.wage = Integer.valueOf(wage);
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

        public Integer getHoursPerWeek() {
            return hoursPerWeek;
        }

        public Integer getWage() {
            return wage;
        }
    }
}

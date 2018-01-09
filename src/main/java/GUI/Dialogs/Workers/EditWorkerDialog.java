package GUI.Dialogs.Workers;

import Entities.Worker;
import GUI.Dialogs.AbstractDialog;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;


public class EditWorkerDialog extends AbstractDialog { // TODO: WHOLE DIALOG
    ButtonType confirmButtonType;
    Worker previousWorker = new Worker();

    public EditWorkerDialog(Worker worker){
        super();
        previousWorker = worker; // used to edit in database

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
        DatePicker hireDateDP = new DatePicker(); // TODO: Date input
        hireDateDP.setPromptText("Hire date");
        TextField hoursPerWeekTF = new TextField();
        hoursPerWeekTF.setPromptText("Working hours per week");
        TextField wageTF = new TextField();
        wageTF.setPromptText("Wage per hour");


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
           // WorkersModification.editWorker(previousWorker, )
            // TODO
        }

    }


    private class Result {
        String name;
        String lastName;
        int pesel;
        Date hireDate;
        Integer hoursPerWeek;
        Float wage;

        public Result(String name, String lastName, String pesel, LocalDate hireDate, String hoursPerWeek, String wage){
            this.name = name;
            this.lastName = lastName;
            this.pesel = Integer.valueOf(pesel);
            this.hireDate = Date.valueOf(hireDate);
            this.hoursPerWeek = Integer.valueOf(hoursPerWeek);
            this.wage = Float.valueOf(wage);
        }

        public String getName() {
            return name;
        }

        public String getLastName() {
            return lastName;
        }

        public int getPesel() {
            return pesel;
        }

        public Date getHireDate() {
            return hireDate;
        }

        public int getHoursPerWeek() {
            return hoursPerWeek;
        }

        public float getWage() {
            return wage;
        }
    }
}

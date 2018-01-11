package GUI.Dialogs.Workers;

import Database.WorkersModification;
import Entities.Worker;
import GUI.Dialogs.AbstractDialog;
import GUI.TextFieldRestrictions;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.Date;
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
        TextField hoursPerWeekTF = new TextField();
        hoursPerWeekTF.setText(workerBeforeEdition.getHoursPerWeek().toString());
        TextField wageTF = new TextField();
        wageTF.setText(workerBeforeEdition.getHoursPerWeek().toString());


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


        TextFieldRestrictions.addIntegerRestriction(hoursPerWeekTF);
        TextFieldRestrictions.addIntegerRestriction(wageTF);
        TextFieldRestrictions.addIntegerRestriction(peselTF);

        TextFieldRestrictions.addTextLimiter(nameTF,32);
        TextFieldRestrictions.addTextLimiter(lastNameTF,32);
        TextFieldRestrictions.addTextLimiter(peselTF,11);
        TextFieldRestrictions.addTextLimiter(hoursPerWeekTF,2);
        TextFieldRestrictions.addTextLimiter(wageTF,6);

        this.getDialogPane().setContent(grid);
        Platform.runLater(() -> nameTF.requestFocus());
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), lastNameTF.getText(), peselTF.getText(), hireDateDP.getValue(), hoursPerWeekTF.getText(), wageTF.getText());
            }
            return null;
        });
    }
    public Worker popDialog(Connection connection){
        Optional<Result> result = this.showAndWait();

        if (result.isPresent()) {
            workerAfterEdition.setName(result.get().getName());
            workerAfterEdition.setLastName(result.get().getLastName());
            workerAfterEdition.setPesel(result.get().getPesel());
            workerAfterEdition.setHireDate(result.get().getHireDate());
            workerAfterEdition.setHoursPerWeek(result.get().getHoursPerWeek());
            workerAfterEdition.setWage(result.get().getWage());
            WorkersModification.editWorker(workerBeforeEdition,workerAfterEdition, connection);
            return workerAfterEdition;
        }
        return null;
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

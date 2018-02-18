package GUI.Dialogs.Holidays;

import Database.HolidaysModification;
import Database.WorkersModification;
import Entities.Holiday;
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

public class AddHolidayDialog extends AbstractDialog {
    private ButtonType confirmButtonType;

    public AddHolidayDialog(){
        super();
        this.setTitle("New holiday");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Worker> workerComboBox = new ComboBox<>();
        try {
            ObservableList<Worker> teamObservableList = FXCollections.observableArrayList(new WorkersModification().importObject());
            workerComboBox.setItems(teamObservableList);
            workerComboBox.setEditable(false);

            workerComboBox.setConverter(new StringConverter<Worker>() {

                @Override
                public String toString(Worker object) {
                    return object.getName() + " " + object.getLastName() + " " + object.getPesel();
                }

                @Override
                public Worker fromString(String string) {
                    return workerComboBox.getItems().stream().filter(ap ->
                            ap.getName().equals(string)).findFirst().orElse(null);
                }
            });
        } catch (SQLException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }

        DatePicker beginDateDP = new DatePicker();
        beginDateDP.setValue(LocalDate.now());
        DatePicker endDateDP = new DatePicker();
        endDateDP.setValue(LocalDate.now().plusDays(1));

        Node loginButton = this.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);
        loginButton.disableProperty().bind(
                workerComboBox.valueProperty().isNull()
                .or( beginDateDP.valueProperty().isNull() )
                .or( endDateDP.valueProperty().isNull() )
        );


        grid.add(new Label("Worker:"), 0, 1);
        grid.add(workerComboBox, 1, 1);
        grid.add(new Label("Begin date:"), 0, 2);
        grid.add(beginDateDP, 1, 2);
        grid.add(new Label("End date:"), 0, 3);
        grid.add(endDateDP, 1, 3);

        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(workerComboBox.getValue().getId(), beginDateDP.getValue(), endDateDP.getValue());
            }
            return null;
        });
    }


    public Holiday popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Holiday holiday = new Holiday();
            holiday.setWorkerId(result.get().getWorkerId());
            holiday.setBeginDate(result.get().getBeginDate());
            holiday.setEndDate(result.get().getEndDate());
            try {
                HolidaysModification.addObject(holiday);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                return null;
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with adding new holiday", "Holiday already in database.").showAndWait();
                return null;
            }
            return holiday;
        }
        return null;
    }

    private class Result {
        private Integer workerId;
        private Date beginDate;
        private Date endDate;

        public Result(Integer workerId, LocalDate beginDate, LocalDate endDate){
            this.workerId = workerId;
            this.beginDate = Date.valueOf(beginDate);
            this.endDate = Date.valueOf(endDate);
        }

        public Integer getWorkerId() {
            return workerId;
        }


        public Date getBeginDate() {
            return beginDate;
        }


        public Date getEndDate() {
            return endDate;
        }
    }
}

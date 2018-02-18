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

public class EditHolidayDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Holiday holidayAfterEdition;
    private Holiday holidayBeforeEdition;

    public EditHolidayDialog(Holiday holiday){
        super();
        holidayBeforeEdition = holiday;
        holidayAfterEdition = new Holiday(holiday);
        this.setTitle("Edit holiday");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Worker> workerComboBox = new ComboBox<>();
        try {
            ObservableList<Worker> workerObservableList = FXCollections.observableArrayList(new WorkersModification().importObject());
            workerComboBox.setItems(workerObservableList);
            workerComboBox.setEditable(false);
            for (int index = 0; index < workerObservableList.size(); index++) {
                if(workerObservableList.get(index).getPesel().equals(holiday.getPesel())){
                    workerComboBox.getSelectionModel().select(index);
                    break;
                }
            }

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
        } catch (SQLException | NullPointerException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        } // TODO: InvalidArgumentException if already in database

        DatePicker beginDateDP = new DatePicker();
        beginDateDP.setValue(holiday.getBeginDate().toLocalDate());
        DatePicker endDateDP = new DatePicker();
        endDateDP.setValue(holiday.getEndDate().toLocalDate());

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
                return new Result(workerComboBox.getValue().getPesel(), beginDateDP.getValue(), endDateDP.getValue());
            }
            return null;
        });
    }

    public Holiday popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            holidayAfterEdition.setPesel(result.get().getPesel());
            holidayAfterEdition.setBeginDate(result.get().getBeginDate());
            holidayAfterEdition.setEndDate(result.get().getEndDate());
            holidayAfterEdition.setId(holidayBeforeEdition.getId());
            try {
                HolidaysModification.editObject(holidayBeforeEdition, holidayAfterEdition);
            } catch (SQLException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                return null;
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with editing the holiday", "Holiday no longer in database.").showAndWait();
                return null;
            }
            return holidayAfterEdition;
        }
        return null;
    }


    private class Result {
        private String pesel;
        private Date beginDate;
        private Date endDate;

        public Result(String pesel, LocalDate beginDate, LocalDate endDate){
            this.pesel = pesel;
            this.beginDate = Date.valueOf(beginDate);
            this.endDate = Date.valueOf(endDate);
        }

        public String getPesel() {
            return pesel;
        }


        public Date getBeginDate() {
            return beginDate;
        }


        public Date getEndDate() {
            return endDate;
        }
    }
}

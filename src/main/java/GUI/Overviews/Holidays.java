package GUI.Overviews;

import Database.HolidaysModification;
import Entities.Holiday;
import GUI.ApplicationGUI;
import GUI.Dialogs.DeleteAlert;
import GUI.Dialogs.ExceptionAlert;
import GUI.Dialogs.Holidays.AddHolidayDialog;
import GUI.Dialogs.Holidays.EditHolidayDialog;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Holidays extends AnchorPane {
    private static final TableView<Holiday> holidaysTable = new TableView<>();
    //private static final TableColumn<Holiday, String> peselColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Holiday, String> beginDateColumn = new TableColumn<>("Begin");
    private static final TableColumn<Holiday, String> endDateColumn = new TableColumn<>("End");

    private Holiday selectedHoliday = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addHolidayButton = new Button("Add");;
    private static Button editHolidayButton = new Button("Edit");
    private static Button deleteHolidayButton = new Button("Delete");
    private static Button backButton = new Button("\u2ba8");

    public static ObservableList<Holiday> holidaysObservableList = FXCollections.emptyObservableList();

    public synchronized final static void refreshTableView() throws SQLException, NullPointerException{
        holidaysObservableList = FXCollections.observableArrayList(new HolidaysModification().importObject());
        holidaysTable.setItems(holidaysObservableList);
    }

    public Holidays(){
        super();
        holidaysTable.setItems(holidaysObservableList);

        //peselColumn.setCellValueFactory(new PropertyValueFactory<Holiday,String>("pesel"));
        beginDateColumn.setCellValueFactory(value -> {
                return new ReadOnlyStringWrapper(value.getValue().getBeginDate().toString());
        });
        endDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getEndDate().toString());
        });

        //holidaysTable.getColumns().addAll(peselColumn, beginDateColumn, endDateColumn);
        holidaysTable.getColumns().addAll(beginDateColumn, endDateColumn);
        holidaysTable.setEditable(false);

        holidaysTable.setColumnResizePolicy((param) -> true);

        holidaysTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedHoliday = holidaysTable.getSelectionModel().getSelectedItem();
            }
        });

        addHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            Holiday newHoliday = new AddHolidayDialog().popDialog();
            if(newHoliday != null) {
                try {
                    holidaysObservableList.add(HolidaysModification.importObject(newHoliday));
                } catch (SQLException | NullPointerException | IllegalArgumentException ex){
                    new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                }
            }
        });

        editHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedHoliday != null){
                Holiday newHoliday = new EditHolidayDialog(selectedHoliday).popDialog();
                if(newHoliday != null) {
                    int index = holidaysObservableList.indexOf(selectedHoliday);
                    holidaysObservableList.remove(index);
                    holidaysObservableList.add(index, newHoliday);
                    selectedHoliday = null;
                    holidaysTable.getSelectionModel().clearSelection();
                }
            }
        });

        deleteHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedHoliday != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        HolidaysModification.deleteObject(selectedHoliday);
                        holidaysObservableList.remove(selectedHoliday);
                        holidaysTable.getSelectionModel().clearSelection();
                        selectedHoliday = null;
                    } catch (SQLException ex){
                        new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                    } catch (IllegalArgumentException ex){
                        new ExceptionAlert("Error with deleting", "Selected holiday no longer in database.").showAndWait();
                        holidaysObservableList.remove(selectedHoliday);
                        holidaysTable.getSelectionModel().clearSelection();
                        selectedHoliday = null;
                    }
                }
            }
        });

        buttons.getButtons().addAll(addHolidayButton, editHolidayButton, deleteHolidayButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            ApplicationGUI.getMainStage().setScene(ApplicationGUI.getMainScene());
        });

        this.getChildren().addAll(holidaysTable,buttons, backButton);
        this.setTopAnchor(holidaysTable,2.0);
        this.setLeftAnchor(holidaysTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
        holidaysTable.setPrefWidth(300);
    }
}

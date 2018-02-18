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
    private static final TableColumn<Holiday, String> peselColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Holiday, String> beginDateColumn = new TableColumn<>("Begin");
    private static final TableColumn<Holiday, String> endDateColumn = new TableColumn<>("End");

    private Holiday selectedHoliday = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addHolidayButton = new Button("Add");;
    private static Button editHolidayButton = new Button("Edit");
    private static Button deleteHolidayButton = new Button("Delete");
    private static Button backButton = new Button("\u2ba8");

    private static ObservableList<Holiday> observableList;


    public final static void refreshTableView(){ // TODO: wywolanie tego dla kazdej zmiany w klasach w paczce Database
        try {
            observableList = FXCollections.observableArrayList(new HolidaysModification().importObject());
            holidaysTable.setItems(observableList);
        } catch (SQLException | NullPointerException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }
    }

    public Holidays(){
        super();

        peselColumn.setCellValueFactory(new PropertyValueFactory<Holiday,String>("pesel"));
        beginDateColumn.setCellValueFactory(value -> {
                return new ReadOnlyStringWrapper(value.getValue().getBeginDate().toString());
        });
        endDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getEndDate().toString());
        });

        holidaysTable.getColumns().addAll(peselColumn, beginDateColumn, endDateColumn);
        holidaysTable.setEditable(false);
        refreshTableView();

        holidaysTable.setColumnResizePolicy((param) -> true);

        holidaysTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedHoliday = holidaysTable.getSelectionModel().getSelectedItem();
            }
        });

        addHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            new AddHolidayDialog().popDialog();
            refreshTableView();  // TODO: fetch Holiday_Id from database rather than refresh whole table
        });

        editHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedHoliday != null){
                Holiday newHoliday = new EditHolidayDialog(selectedHoliday).popDialog();
                if(newHoliday != null) {
                    int index = observableList.indexOf(selectedHoliday);
                    observableList.remove(index);
                    observableList.add(index, newHoliday);
                    selectedHoliday = null;
                    holidaysTable.getSelectionModel().clearSelection();
                }
            } // TODO: if no longer in database, remove from tableview / refresh
        });

        deleteHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedHoliday != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        HolidaysModification.deleteObject(selectedHoliday);
                        observableList.remove(selectedHoliday);
                        holidaysTable.getSelectionModel().clearSelection();
                        selectedHoliday = null;
                    } catch (SQLException ex){
                        new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                    } catch (IllegalArgumentException ex){
                        new ExceptionAlert("Error with deleting", "Selected holiday no longer in database.").showAndWait();
                        observableList.remove(selectedHoliday);
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

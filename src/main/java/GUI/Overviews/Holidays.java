package GUI.Overviews;

import Database.HolidaysModification;
import Entities.Holiday;
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

public class Holidays extends AnchorPane {
    private static final TableView<Holiday> holidaysTable = new TableView<>();
    private static final TableColumn<Holiday, String> peselColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Holiday, String> beginDateColumn = new TableColumn<>("Begin");
    private static final TableColumn<Holiday, String> endDateColumn = new TableColumn<>("End");

    private Holiday selectedHoliday = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addHolidayButton = new Button("Add");;
    private static Button editHolidayButton = new Button("Edit");
    private static Button backButton = new Button("\u2ba8");


    private void refreshTableView(){
        ObservableList<Holiday> observableList = FXCollections.observableArrayList(new HolidaysModification().importObject());
        holidaysTable.setItems(observableList);
    }

    public Holidays(Stage mainStage, Scene mainScene){
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
            refreshTableView(); // TODO refresh tylko dla nowo dodanego
        });

        editHolidayButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedHoliday != null){
                new EditHolidayDialog(selectedHoliday).popDialog();
            }
            refreshTableView(); // TODO refresh tylko dla edytowanego
        });
        // TODO removeHolidayButton

        buttons.getButtons().addAll(addHolidayButton, editHolidayButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            mainStage.setScene(mainScene);
        });

        this.getChildren().addAll(holidaysTable,buttons, backButton);
        this.setTopAnchor(holidaysTable,2.0);
        this.setLeftAnchor(holidaysTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
    }
}
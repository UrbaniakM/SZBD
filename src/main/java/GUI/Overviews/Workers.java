package GUI.Overviews;

import Entities.Worker;
import Features.WorkersDisplay;
import GUI.Workers.AddWorkerDialog;
import GUI.Workers.EditWorkerDialog;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class Workers extends AnchorPane{
    private TableView<Worker> workersTable = new TableView<>();
    private TableColumn firstNameColumn = new TableColumn("First name");
    private TableColumn lastNameColumn = new TableColumn("Last name");
    private TableColumn peselColumn = new TableColumn("Pesel");

    private Worker selectedWorker = null;

    private static Label name = new Label();
    private static Label lastName = new Label();
    private static Label pesel = new Label();
    private static Label hireData = new Label();
    private static Label fireData = new Label();
    private static Label hoursPerWeek = new Label();
    private static Label wage = new Label();

    private static Button addWorkerButton = new Button("Add");;
    private static Button editWorkerButton = new Button("Edit");

    private static HBox buttons = new HBox();
    private static HBox display = new HBox();
    private static VBox moreData = new VBox();

    public Workers(Stage mainStage, Connection connection){
        super();
        firstNameColumn.setCellFactory(new PropertyValueFactory<Worker,String>("name"));
        lastNameColumn.setCellFactory(new PropertyValueFactory<Worker,String>("lastName"));
        peselColumn.setCellFactory(new PropertyValueFactory<Worker,Integer>("pesel"));
        workersTable.getColumns().addAll(firstNameColumn, lastNameColumn, peselColumn);

        ObservableList<Worker> observableList = FXCollections.observableArrayList(new WorkersDisplay().importWorkers(connection));
        workersTable.setItems(observableList);

        workersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                selectedWorker = workersTable.getSelectionModel().getSelectedItem();
            }
        });

        moreData.getChildren().addAll(name, lastName, pesel, hireData, fireData, hoursPerWeek, wage);

        display.getChildren().addAll(workersTable, moreData);

        addWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            new AddWorkerDialog().popDialog();
        });

        editWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
                new EditWorkerDialog(selectedWorker).popDialog();
            }
        });

        buttons.setSpacing(2);
        buttons.getChildren().addAll(addWorkerButton, editWorkerButton);

        this.getChildren().addAll(display,buttons);
        this.setTopAnchor(display,2.0);
        this.setLeftAnchor(display,2.0);
        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
    }
}
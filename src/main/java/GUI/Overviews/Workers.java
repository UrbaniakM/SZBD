package GUI.Overviews;

import Entities.Worker;
import Database.WorkersModification;
import GUI.Dialogs.Workers.AddWorkerDialog;
import GUI.Dialogs.Workers.EditWorkerDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class Workers extends AnchorPane{
    private Stage mainStage;
    private Scene mainScene;

    private TableView<Worker> workersTable = new TableView<>();
    private TableColumn<Worker, String> firstNameColumn = new TableColumn<>("First name");
    private TableColumn<Worker, String> lastNameColumn = new TableColumn<>("Last name");
    private TableColumn<Worker, Integer> peselColumn = new TableColumn<>("Pesel");

    private Worker selectedWorker = null;

    private static Label name = new Label();
    private static Label lastName = new Label();
    private static Label pesel = new Label();
    private static Label hireData = new Label();
    private static Label fireData = new Label();
    private static Label hoursPerWeek = new Label();
    private static Label wage = new Label();

    private static ButtonBar buttons = new ButtonBar();
    private static Button addWorkerButton = new Button("Add");;
    private static Button editWorkerButton = new Button("Edit");
    private static Button backButton = new Button("\u2ba8");


    private static HBox display = new HBox();
    private static VBox moreData = new VBox();

    public Workers(Stage mainStage, Scene mainScene, Connection connection){
        super();
        this.mainStage = mainStage; // TODO: button do powrotu do glownej sceny
        this.mainScene = mainScene;

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<Worker,Integer>("pesel"));
        workersTable.getColumns().addAll(firstNameColumn, lastNameColumn, peselColumn);
        workersTable.setEditable(false);
        ObservableList<Worker> observableList = FXCollections.observableArrayList(new WorkersModification().importWorkers(connection));
        workersTable.setItems(observableList);

        workersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                selectedWorker = workersTable.getSelectionModel().getSelectedItem();
            }
        });

        moreData.getChildren().addAll(name, lastName, pesel, hireData, fireData, hoursPerWeek, wage);
        display.getChildren().addAll(workersTable, moreData);

        addWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            new AddWorkerDialog().popDialog(connection);
        });

        editWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
                new EditWorkerDialog(selectedWorker).popDialog(connection);
            }
        });

        buttons.getButtons().addAll(addWorkerButton, editWorkerButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            mainStage.setScene(mainScene);
        });

        this.getChildren().addAll(display,buttons, backButton);
        this.setTopAnchor(display,2.0);
        this.setLeftAnchor(display,2.0);
        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
    }
}

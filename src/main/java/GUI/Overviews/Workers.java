package GUI.Overviews;

import Database.WorkersModification;
import Entities.Worker;
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
import java.text.Format;
import java.text.SimpleDateFormat;

public class Workers extends AnchorPane{
    private Stage mainStage;
    private Scene mainScene;

    private TableView<Worker> workersTable = new TableView<>();
    private TableColumn<Worker, String> firstNameColumn = new TableColumn<>("First name");
    private TableColumn<Worker, String> lastNameColumn = new TableColumn<>("Last name");
    private TableColumn<Worker, Integer> peselColumn = new TableColumn<>("Pesel");

    private Worker selectedWorker = null;

    private static Label nameLabel = new Label();
    private static Label lastNameLabel = new Label();
    private static Label peselLabel = new Label();
    private static Label hireDataLabel = new Label();
    private static Label fireDataLabel = new Label();
    private static Label hoursPerWeekLabel = new Label();
    private static Label wageLabel = new Label();

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
            if (event.getClickCount() >= 1) {
                selectedWorker = workersTable.getSelectionModel().getSelectedItem();
                showWorkerDetail();
            }
        });

        moreData.getChildren().addAll(nameLabel, lastNameLabel, peselLabel, hireDataLabel, fireDataLabel, hoursPerWeekLabel, wageLabel);
        display.getChildren().addAll(workersTable, moreData);

        addWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            observableList.add(new AddWorkerDialog().popDialog(connection));
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

    private void showWorkerDetail(){
        if(selectedWorker != null){
            nameLabel.setText(selectedWorker.getName());
            lastNameLabel.setText(selectedWorker.getLastName());
            peselLabel.setText(selectedWorker.getPesel());
            Format formatter = new SimpleDateFormat("dd-MM-yyyy");
            if(selectedWorker.getHireDate() != null) {
                hireDataLabel.setText(formatter.format(selectedWorker.getHireDate()));
            } else {
                hireDataLabel.setText("");
            }
            if(selectedWorker.getFireDate() != null) {
                fireDataLabel.setText(formatter.format(selectedWorker.getFireDate()));
            } else {
                fireDataLabel.setText("");
            }
            if(selectedWorker.getHoursPerWeek() != null) {
                hoursPerWeekLabel.setText(String.valueOf(selectedWorker.getHoursPerWeek()));
            } else {
                hoursPerWeekLabel.setText("");
            }
            if(selectedWorker.getWage() != null) {
                wageLabel.setText(String.valueOf(selectedWorker.getWage()));
            } else {
                wageLabel.setText("");
            }
        } else {
            nameLabel.setText("");
            lastNameLabel.setText("");
            peselLabel.setText("");
            hireDataLabel.setText("");
            fireDataLabel.setText("");
            hoursPerWeekLabel.setText("");
            wageLabel.setText("");
        }
    }
}

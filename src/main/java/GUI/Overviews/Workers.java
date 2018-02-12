package GUI.Overviews;

import Database.WorkersModification;
import Entities.Worker;
import GUI.Dialogs.Workers.AddWorkerDialog;
import GUI.Dialogs.Workers.EditWorkerDialog;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Workers extends AnchorPane{
    private Connection connection;

    private static final TableView<Worker> workersTable = new TableView<>();
    private static final TableColumn<Worker, String> firstNameColumn = new TableColumn<>("First name");
    private static final TableColumn<Worker, String> lastNameColumn = new TableColumn<>("Last name");
    private static final TableColumn<Worker, String> peselColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Worker, String> hireDateColumn = new TableColumn<>("Hire date");
    private static final TableColumn<Worker, String> bonusColumn = new TableColumn<>("Bonus");
    private static final TableColumn<Worker, String> positionNameColumn = new TableColumn<>("Position");
    private static final TableColumn<Worker, String> teamNameColumn = new TableColumn<>("Team");

    private Worker selectedWorker = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addWorkerButton = new Button("Add");;
    private static Button editWorkerButton = new Button("Edit");
    private static Button backButton = new Button("\u2ba8");


    private void refreshTableView(){
        ObservableList<Worker> observableList = FXCollections.observableArrayList(new WorkersModification().importWorkers(connection));
        workersTable.setItems(observableList);
    }

    public Workers(Stage mainStage, Scene mainScene, Connection connection){
        super();
        this.connection = connection;

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("pesel"));
        hireDateColumn.setCellValueFactory(value -> {
            if(value.getValue().getHireDate() != null) {
                return new ReadOnlyStringWrapper(value.getValue().getHireDate().toString());
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });
        bonusColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("bonus"));
        positionNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("positionName"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("teamName"));
        workersTable.getColumns().addAll(firstNameColumn, lastNameColumn, peselColumn, hireDateColumn, bonusColumn, positionNameColumn, teamNameColumn);
        workersTable.setEditable(false);
        refreshTableView();

        workersTable.setColumnResizePolicy((param) -> true);

        workersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedWorker = workersTable.getSelectionModel().getSelectedItem();
                //showWorkerDetail();
            }
        });

        addWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            new AddWorkerDialog().popDialog(connection);
            refreshTableView(); // TODO refresh tylko dla nowo dodanego
        });

        editWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
               new EditWorkerDialog(selectedWorker).popDialog(connection);
            }
            refreshTableView(); // TODO refresh tylko dla edytowanego
        });

        buttons.getButtons().addAll(addWorkerButton, editWorkerButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            mainStage.setScene(mainScene);
        });

        this.getChildren().addAll(workersTable,buttons, backButton);
        this.setTopAnchor(workersTable,2.0);
        this.setLeftAnchor(workersTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
    }

    /*private void showWorkerDetail(){
        if(selectedWorker != null){
            nameLabel.setText(selectedWorker.getName());
            lastNameLabel.setText(selectedWorker.getLastName());
            peselLabel.setText(selectedWorker.getPesel());
            Format formatter = new SimpleDateFormat("dd-MM-yyyy");
            if(selectedWorker.getHireDate() != null) {
                hireDateLabel.setText(formatter.format(selectedWorker.getHireDate()));
            } else {
                hireDateLabel.setText("");
            }
            if(selectedWorker.getFireDate() != null) {
                fireDateLabel.setText(formatter.format(selectedWorker.getFireDate()));
            } else {
                fireDateLabel.setText("");
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
            hireDateLabel.setText("");
            fireDateLabel.setText("");
            hoursPerWeekLabel.setText("");
            wageLabel.setText("");
        }
    }*/
}

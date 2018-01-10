package GUI.Overviews;

import Database.WorkersModification;
import Entities.Worker;
import GUI.Dialogs.Workers.AddWorkerDialog;
import GUI.Dialogs.Workers.EditWorkerDialog;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
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

    private TableView<Worker> workersTable = new TableView<>();
    private TableColumn<Worker, String> firstNameColumn = new TableColumn<>("First name");
    private TableColumn<Worker, String> lastNameColumn = new TableColumn<>("Last name");
    private TableColumn<Worker, String> peselColumn = new TableColumn<>("Pesel");
    private TableColumn<Worker, String> hireDateColumn = new TableColumn<>("Hire date");
    private TableColumn<Worker, String> fireDateColumn = new TableColumn<>("Fire date");
    private TableColumn<Worker, String> hoursPerWeekColumn = new TableColumn<>("Hours per week");
    private TableColumn<Worker, String> wageColumn = new TableColumn<>("Wage");

    private Worker selectedWorker = null;

    private static Label nameLabel = new Label();
    private static Label lastNameLabel = new Label();
    private static Label peselLabel = new Label();
    private static Label hireDateLabel = new Label();
    private static Label fireDateLabel = new Label();
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

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("pesel"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("hireDate"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("fireDate"));
        hoursPerWeekColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("hoursPerWeek"));
        wageColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("wage"));
        workersTable.getColumns().addAll(firstNameColumn, lastNameColumn, peselColumn, hireDateColumn, fireDateColumn, hoursPerWeekColumn, wageColumn);
        workersTable.setEditable(false);
        ObservableList<Worker> observableList = FXCollections.observableArrayList(new WorkersModification().importWorkers(connection));
        workersTable.setItems(observableList);

        workersTable.setColumnResizePolicy((param) -> true);

        workersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedWorker = workersTable.getSelectionModel().getSelectedItem();
                showWorkerDetail();
            }
        });

        moreData.getChildren().addAll( // TODO: flowpane
                new Label("Selected worker details:"),
                new HBox(new Label("Name: "),nameLabel),
                new HBox(new Label("Last name: "),lastNameLabel),
                new HBox(new Label("Pesel: "),peselLabel),
                new HBox(new Label("Hire date: "),hireDateLabel),
                new HBox(new Label("Fire date: "),fireDateLabel),
                new HBox(new Label("Hours per week: "),hoursPerWeekLabel),
                new HBox(new Label("Wage: "),wageLabel)
        );
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
    }
}

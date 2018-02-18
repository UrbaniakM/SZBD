package GUI.Overviews;

import Database.WorkersModification;
import Entities.Worker;
import GUI.ApplicationGUI;
import GUI.Dialogs.DeleteAlert;
import GUI.Dialogs.ExceptionAlert;
import GUI.Dialogs.Workers.AddWorkerDialog;
import GUI.Dialogs.Workers.EditWorkerDialog;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLDataException;
import java.sql.SQLException;

public class Workers extends AnchorPane{

    private static final TableView<Worker> workersTable = new TableView<>();
    private static final TableColumn<Worker, String> firstNameColumn = new TableColumn<>("First name");
    private static final TableColumn<Worker, String> lastNameColumn = new TableColumn<>("Last name");
    private static final TableColumn<Worker, String> peselColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Worker, String> hireDateColumn = new TableColumn<>("Hired");
    private static final TableColumn<Worker, String> bonusColumn = new TableColumn<>("Bonus");
    private static final TableColumn<Worker, String> positionNameColumn = new TableColumn<>("Position");
    private static final TableColumn<Worker, String> teamNameColumn = new TableColumn<>("Team");

    private Worker selectedWorker = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addWorkerButton = new Button("Add");;
    private static Button editWorkerButton = new Button("Edit");
    private static Button deleteWorkerButton = new Button("Delete");
    private static Button backButton = new Button("\u2ba8");

    private static ObservableList<Worker> observableList;

    public final static void refreshTableView(){
        try {
            observableList = FXCollections.observableArrayList(new WorkersModification().importObject());
            workersTable.setItems(observableList);
        } catch (SQLException | NullPointerException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }
    }

    public Workers(){
        super();

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("lastName"));
        peselColumn.setCellValueFactory(new PropertyValueFactory<Worker,String>("pesel"));
        hireDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getHireDate().toString());
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
            }
        });

        addWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            Worker newWorker = new AddWorkerDialog().popDialog();
            if(newWorker != null){
                observableList.add(newWorker);
            }
        });

        editWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
                Worker newWorker = new EditWorkerDialog(selectedWorker).popDialog();
                if(newWorker != null) {
                    int index = observableList.indexOf(selectedWorker);
                    observableList.remove(index);
                    observableList.add(index, newWorker);
                    selectedWorker = null;
                    workersTable.getSelectionModel().clearSelection();
                }
            }  // TODO: if no longer in database, remove from tableview / refresh
        });
        deleteWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        WorkersModification.deleteObject(selectedWorker);
                        observableList.remove(selectedWorker);
                        workersTable.getSelectionModel().clearSelection();
                        selectedWorker = null;
                    } catch (SQLDataException ex){
                            new ExceptionAlert("Error with deleting",
                                    "This worker is assigned as a leader to at least one team. " +
                                            "Delete the team(teams) or change its(their) leader before deleting this worker.").showAndWait();
                    } catch (SQLException ex){
                        new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                    } catch (IllegalArgumentException ex){
                        new ExceptionAlert("Error with deleting", "Selected holiday no longer in database.").showAndWait();
                        observableList.remove(selectedWorker);
                        workersTable.getSelectionModel().clearSelection();
                        selectedWorker = null;
                    }
                }
            }
        });

        buttons.getButtons().addAll(addWorkerButton, editWorkerButton, deleteWorkerButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            ApplicationGUI.getMainStage().setScene(ApplicationGUI.getMainScene());
        });

        this.getChildren().addAll(workersTable,buttons, backButton);
        this.setTopAnchor(workersTable,2.0);
        this.setLeftAnchor(workersTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
        workersTable.setPrefWidth(830);
    }
}

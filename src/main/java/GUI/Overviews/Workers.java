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

    public static ObservableList<Worker> workersObservableList = FXCollections.observableArrayList();

    private synchronized static void removeObject(Worker worker){
        workersObservableList.remove(worker);
    }

    private synchronized static void editObject(Worker prev, Worker act){
        int index = workersObservableList.indexOf(prev);
        workersObservableList.remove(index);
        workersObservableList.add(index,act);
    }

    public synchronized final static void refreshTableView() throws NullPointerException, SQLException{
        workersObservableList = FXCollections.observableArrayList(new WorkersModification().importObject());
        workersTable.setItems(workersObservableList);
    }

    public Workers(){
        super();
        workersTable.setItems(workersObservableList);

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

        workersTable.setColumnResizePolicy((param) -> true);

        workersTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedWorker = workersTable.getSelectionModel().getSelectedItem();
            }
        });

        addWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            new AddWorkerDialog().popDialog();
        });

        editWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
                Worker newWorker = new EditWorkerDialog(selectedWorker).popDialog();
                if(newWorker != null) {
                    //editObject(selectedWorker,newWorker);
                    selectedWorker = null;
                    workersTable.getSelectionModel().clearSelection();
                }
            }
        });

        deleteWorkerButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedWorker != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        WorkersModification.deleteObject(selectedWorker);
                        //removeObject(selectedWorker);
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
                        workersObservableList.remove(selectedWorker);
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

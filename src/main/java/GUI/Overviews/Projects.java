package GUI.Overviews;

import Database.ProjectsModification;
import Entities.Project;
import GUI.ApplicationGUI;
import GUI.Dialogs.DeleteAlert;
import GUI.Dialogs.ExceptionAlert;
import GUI.Dialogs.Projects.AddProjectDialog;
import GUI.Dialogs.Projects.EditProjectDialog;
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

public class Projects extends AnchorPane{
    private static final TableView<Project> projectsTable = new TableView<>();
    private static final TableColumn<Project, String> nameColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Project, String> beginDateColumn = new TableColumn<>("Began");
    private static final TableColumn<Project, String> endDateColumn = new TableColumn<>("Ended");
    private static final TableColumn<Project, String> teamColumn = new TableColumn<>("Team");

    private Project selectedProject = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addProjectButton = new Button("Add");;
    private static Button editProjectButton = new Button("Edit");
    private static Button deleteProjectButton = new Button("Delete");
    private static Button backButton = new Button("\u2ba8");

    public static ObservableList<Project> projectsObservableList = FXCollections.observableArrayList();

    private synchronized static void removeObject(Project project){
        projectsObservableList.remove(project);
    }

    private synchronized static void editObject(Project prev, Project act){
        int index = projectsObservableList.indexOf(prev);
        projectsObservableList.remove(index);
        projectsObservableList.add(index,act);
    }

    public synchronized final static void refreshTableView() throws SQLException, NullPointerException{
        projectsObservableList = FXCollections.observableArrayList(new ProjectsModification().importObject());
        projectsTable.setItems(projectsObservableList);
    }

    public Projects(){
        super();
        projectsTable.setItems(projectsObservableList);

        nameColumn.setCellValueFactory(new PropertyValueFactory<Project,String>("name"));
        beginDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getBeginDate().toString());
        });
        endDateColumn.setCellValueFactory(value -> {
            if(value.getValue().getEndDate() != null) {
                return new ReadOnlyStringWrapper(value.getValue().getEndDate().toString());
            } else {
                return new ReadOnlyStringWrapper("");
            }
        });
        teamColumn.setCellValueFactory(new PropertyValueFactory<Project,String>("teamName"));

        projectsTable.getColumns().addAll(nameColumn, beginDateColumn, endDateColumn, teamColumn);
        projectsTable.setEditable(false);

        projectsTable.setColumnResizePolicy((param) -> true);

        projectsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedProject = projectsTable.getSelectionModel().getSelectedItem();
            }
        });

        addProjectButton.setOnMouseClicked((MouseEvent event) -> {
            new AddProjectDialog().popDialog();
        });

        editProjectButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedProject != null){
                Project newProject = new EditProjectDialog(selectedProject).popDialog();
                if(newProject != null) {
                    int index = projectsObservableList.indexOf(selectedProject);
                    //editObject(selectedProject, newProject);
                    selectedProject = null;
                    projectsTable.getSelectionModel().clearSelection();
                }
            }
        });
        deleteProjectButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedProject != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        ProjectsModification.deleteObject(selectedProject);
                        //removeObject(selectedProject);
                        projectsTable.getSelectionModel().clearSelection();
                        selectedProject = null;
                    } catch (SQLException ex){
                        new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                    } catch (IllegalArgumentException ex){
                        new ExceptionAlert("Error with deleting", "Selected project no longer in database.").showAndWait();
                        projectsObservableList.remove(selectedProject);
                        projectsTable.getSelectionModel().clearSelection();
                        selectedProject = null;
                    }
                }
            }
        });

        buttons.getButtons().addAll(addProjectButton, editProjectButton, deleteProjectButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            ApplicationGUI.getMainStage().setScene(ApplicationGUI.getMainScene());
        });

        this.getChildren().addAll(projectsTable,buttons, backButton);
        this.setTopAnchor(projectsTable,2.0);
        this.setLeftAnchor(projectsTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
        projectsTable.setPrefWidth(430);
    }
}

package GUI.Overviews;

import Database.ProjectsModification;
import Entities.Project;
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

public class Projects extends AnchorPane{
    private static final TableView<Project> projectsTable = new TableView<>();
    private static final TableColumn<Project, String> nameColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Project, String> beginDateColumn = new TableColumn<>("Began");
    private static final TableColumn<Project, String> endDateColumn = new TableColumn<>("Ended");
    private static final TableColumn<Project, String> teamColumn = new TableColumn<>("Team");

    private Project selectedProject = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addTeamButton = new Button("Add");;
    private static Button editTeamButton = new Button("Edit");
    private static Button backButton = new Button("\u2ba8");


    private void refreshTableView(){
        ObservableList<Project> observableList = FXCollections.observableArrayList(new ProjectsModification().importObject());
        projectsTable.setItems(observableList);
    }

    public Projects(Stage mainStage, Scene mainScene){
        super();

        nameColumn.setCellValueFactory(new PropertyValueFactory<Project,String>("name"));
        beginDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getBeginDate().toString());
        });
        endDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getEndDate().toString());
        });
        teamColumn.setCellValueFactory(new PropertyValueFactory<Project,String>("teamName"));

        projectsTable.getColumns().addAll(nameColumn, beginDateColumn, endDateColumn, teamColumn);
        projectsTable.setEditable(false);
        refreshTableView();

        projectsTable.setColumnResizePolicy((param) -> true);

        projectsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedProject = projectsTable.getSelectionModel().getSelectedItem();
            }
        });

        addTeamButton.setOnMouseClicked((MouseEvent event) -> {
            new AddProjectDialog().popDialog();
            refreshTableView(); // TODO refresh tylko dla nowo dodanego
        });

        editTeamButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedProject != null){
                new EditProjectDialog(selectedProject).popDialog();
            }
            refreshTableView(); // TODO refresh tylko dla edytowanego
        });
        // TODO removeHolidayButton

        buttons.getButtons().addAll(addTeamButton, editTeamButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            mainStage.setScene(mainScene);
        });

        this.getChildren().addAll(projectsTable,buttons, backButton);
        this.setTopAnchor(projectsTable,2.0);
        this.setLeftAnchor(projectsTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
    }
}
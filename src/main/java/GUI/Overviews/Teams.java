package GUI.Overviews;

import Database.TeamsModification;
import Entities.Team;
import GUI.ApplicationGUI;
import GUI.Dialogs.DeleteAlert;
import GUI.Dialogs.ExceptionAlert;
import GUI.Dialogs.Teams.AddTeamDialog;
import GUI.Dialogs.Teams.EditTeamDialog;
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

import java.sql.SQLDataException;
import java.sql.SQLException;

public class Teams extends AnchorPane{
    private static final TableView<Team> teamsTable = new TableView<>();
    private static final TableColumn<Team, String> nameColumn = new TableColumn<>("Pesel");
    private static final TableColumn<Team, String> creationDateColumn = new TableColumn<>("Created");
    private static final TableColumn<Team, String> peselLeaderColumn = new TableColumn<>("Leader (PESEL)");

    private Team selectedTeam = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addTeamButton = new Button("Add");;
    private static Button editTeamButton = new Button("Edit");
    private static Button deleteTeamButton = new Button("Delete");
    private static Button backButton = new Button("\u2ba8");

    private static ObservableList<Team> observableList;

    public final static void refreshTableView(){
        try {
            observableList = FXCollections.observableArrayList(new TeamsModification().importObject());
            teamsTable.setItems(observableList);
        } catch (SQLException | NullPointerException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }
    }

    public Teams(){
        super();

        nameColumn.setCellValueFactory(new PropertyValueFactory<Team,String>("name"));
        creationDateColumn.setCellValueFactory(value -> {
            return new ReadOnlyStringWrapper(value.getValue().getCreationDate().toString());
        });
        peselLeaderColumn.setCellValueFactory(new PropertyValueFactory<Team,String>("leaderPesel"));

        teamsTable.getColumns().addAll(nameColumn, creationDateColumn, peselLeaderColumn);
        teamsTable.setEditable(false);
        refreshTableView();

        teamsTable.setColumnResizePolicy((param) -> true);

        teamsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
            }
        });

        addTeamButton.setOnMouseClicked((MouseEvent event) -> {
            Team newTeam = new AddTeamDialog().popDialog();
            if(newTeam != null){
                observableList.add(newTeam);
            }
        });

        editTeamButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedTeam != null){
                Team newTeam = new EditTeamDialog(selectedTeam).popDialog();
                if(newTeam != null) {
                    int index = observableList.indexOf(selectedTeam);
                    observableList.remove(index);
                    observableList.add(index, newTeam);
                    selectedTeam = null;
                    teamsTable.getSelectionModel().clearSelection();
                }
            }  // TODO: if no longer in database, remove from tableview / refresh
        });
        deleteTeamButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedTeam != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        TeamsModification.deleteObject(selectedTeam);
                        observableList.remove(selectedTeam);
                        teamsTable.getSelectionModel().clearSelection();
                        selectedTeam = null;
                    } catch (SQLDataException ex){
                        if(ex.getMessage().equals("Team in projects table.")){
                            new ExceptionAlert("Error with deleting",
                                    "This team is assigned to at least one project. " +
                                            "Delete the project(projects) or change its(their) team before deleting this team.").showAndWait();
                        } else{
                            new ExceptionAlert("Error with deleting",
                                    "This team is assigned to at least one worker. " +
                                            "Delete the worker(workers) or change his(their) team before deleting this team.").showAndWait();
                        }
                    } catch (SQLException ex){
                        new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                    } catch (IllegalArgumentException ex){
                        new ExceptionAlert("Error with deleting", "Selected holiday no longer in database.").showAndWait();
                        observableList.remove(selectedTeam);
                        teamsTable.getSelectionModel().clearSelection();
                        selectedTeam = null;
                    }
                }
            }
        });
        buttons.getButtons().addAll(addTeamButton, editTeamButton, deleteTeamButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            ApplicationGUI.getMainStage().setScene(ApplicationGUI.getMainScene());
        });

        this.getChildren().addAll(teamsTable,buttons, backButton);
        this.setTopAnchor(teamsTable,2.0);
        this.setLeftAnchor(teamsTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
        teamsTable.setPrefWidth(400);
    }
}

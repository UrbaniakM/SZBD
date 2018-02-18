package GUI.Overviews;

import Database.TeamsModification;
import Entities.Team;
import GUI.ApplicationGUI;
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
    private static Button backButton = new Button("\u2ba8");


    private void refreshTableView(){
        try {
            ObservableList<Team> observableList = FXCollections.observableArrayList(new TeamsModification().importObject());
            teamsTable.setItems(observableList);
        } catch (SQLException ex){
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
            new AddTeamDialog().popDialog();
            refreshTableView(); // TODO refresh tylko dla nowo dodanego
        });

        editTeamButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedTeam != null){
                new EditTeamDialog(selectedTeam).popDialog();
                refreshTableView(); // TODO refresh tylko dla edytowanego
                selectedTeam = null;
            }
        });
        // TODO removeHolidayButton

        buttons.getButtons().addAll(addTeamButton, editTeamButton);

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

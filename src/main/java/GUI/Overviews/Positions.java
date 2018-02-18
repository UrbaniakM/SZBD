package GUI.Overviews;

import Database.PositionsModification;
import Entities.Position;
import GUI.ApplicationGUI;
import GUI.Dialogs.DeleteAlert;
import GUI.Dialogs.ExceptionAlert;
import GUI.Dialogs.Positions.AddPositionDialog;
import GUI.Dialogs.Positions.EditPositionDialog;
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

public class Positions extends AnchorPane {
    private static final TableView<Position> positionsTable = new TableView<>();
    private static final TableColumn<Position, String> nameColumn = new TableColumn<>("Name");
    private static final TableColumn<Position, String> wageColumn = new TableColumn<>("Wage");

    private Position selectedPosition = null;

    private static ButtonBar buttons = new ButtonBar();
    private static Button addPositionButton = new Button("Add");;
    private static Button editPositionButton = new Button("Edit");
    private static Button deletePositionButton = new Button("Delete");
    private static Button backButton = new Button("\u2ba8");


    private void refreshTableView(){
        try {
            ObservableList<Position> observableList = FXCollections.observableArrayList(new PositionsModification().importObject());
            positionsTable.setItems(observableList);
        } catch (SQLException ex){
            new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
        }
    }

    public Positions(){
        super();

        nameColumn.setCellValueFactory(new PropertyValueFactory<Position,String>("name"));
        wageColumn.setCellValueFactory(new PropertyValueFactory<Position,String>("wage"));

        positionsTable.getColumns().addAll(nameColumn, wageColumn);
        positionsTable.setEditable(false);
        refreshTableView();

        positionsTable.setColumnResizePolicy((param) -> true);

        positionsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedPosition = positionsTable.getSelectionModel().getSelectedItem();
            }
        });

        addPositionButton.setOnMouseClicked((MouseEvent event) -> {
            new AddPositionDialog().popDialog();
            refreshTableView(); // TODO refresh tylko dla nowo dodanego
        });

        editPositionButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedPosition != null){
                new EditPositionDialog(selectedPosition).popDialog();
                refreshTableView(); // TODO refresh tylko dla edytowanego
                selectedPosition = null;
            }
        });
        deletePositionButton.setOnMouseClicked((MouseEvent event) -> {
            if(selectedPosition != null){
                if(new DeleteAlert().popDialog()){
                    try{
                        PositionsModification.deleteObject(selectedPosition);
                        refreshTableView(); // TODO refresh tylko dla edytowanego
                        selectedPosition = null;
                    } catch (SQLDataException ex){
                        new ExceptionAlert("Error with deleting",
                                "This position in assigned to at least one worker. " +
                                        "Delete the worker or change his position before deleting this position.").showAndWait();
                    } catch (SQLException ex){
                        ex.printStackTrace();
                        new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                    } catch (IllegalArgumentException ex){
                        new ExceptionAlert("Error with deleting", "Selected holiday no longer in database.").showAndWait();
                    }
                }
            }
        });

        buttons.getButtons().addAll(addPositionButton, editPositionButton, deletePositionButton);

        backButton.setOnMouseClicked((MouseEvent event) -> {
            ApplicationGUI.getMainStage().setScene(ApplicationGUI.getMainScene());
        });

        this.getChildren().addAll(positionsTable,buttons, backButton);
        this.setTopAnchor(positionsTable,2.0);
        this.setLeftAnchor(positionsTable,2.0);

        this.setRightAnchor(buttons,2.0);
        this.setBottomAnchor(buttons,4.0);
        this.setTopAnchor(backButton, 2.0);
        this.setLeftAnchor(backButton, 2.0);
        positionsTable.setPrefWidth(300);
    }
}

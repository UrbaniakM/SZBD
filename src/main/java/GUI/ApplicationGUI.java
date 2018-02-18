package GUI;

import Database.*;
import Entities.Position;
import GUI.Dialogs.LoginDialog;
import GUI.Overviews.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.sql.Connection;

public class ApplicationGUI extends Application {
    public static DatabaseConnection databaseConnection;

    private static Stage mainStage;
    private static Scene mainScene;

    private static Tile workersTile = new Tile(150);
    private static Tile teamsTile = new Tile(150);
    private static Tile projectsTile = new Tile(150);
    private static Tile positionsTile = new Tile(150);
    private static Tile holidaysTile = new Tile(150);

    public static void main(String[] args) {
        launch(args);
    }

    private Scene mainGUI(){
        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 5, 5, 5));
        flowPane.setVgap(4);
        flowPane.setHgap(4);

        workersTile.setText("Workers");
        teamsTile.setText("Teams");
        projectsTile.setText("Projects");
        positionsTile.setText("Positions");
        holidaysTile.setText("Holidays");

        flowPane.getChildren().addAll(workersTile, teamsTile, projectsTile, positionsTile, holidaysTile);

        return new Scene(flowPane, 468, 314);
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static Scene getMainScene(){
        return mainScene;
    }

    @Override
    public void start(Stage mainStage) {
        this.mainStage = mainStage;
        LoginDialog loginDialog = new LoginDialog();
        databaseConnection = new DatabaseConnection(loginDialog.getUsername(), loginDialog.getPassword());

        mainScene = mainGUI();
        mainStage.setResizable(false);

        workersTile.changeMainContent(new Scene(new Workers()));
        teamsTile.changeMainContent(new Scene(new Teams()));
        projectsTile.changeMainContent(new Scene(new Projects()));
        positionsTile.changeMainContent(new Scene(new Positions()));
        holidaysTile.changeMainContent(new Scene(new Holidays()));

        try { // TODO: new content, delete print in console
            System.out.println("Workers: " + WorkersModification.countObjects());
            System.out.println("Projects: " + ProjectsModification.countObjects());
            System.out.println("Teams: " + TeamsModification.countObjects());
            System.out.println("Positions: " + PositionsModification.countObjects());
        } catch (Exception ex){
            ex.printStackTrace();
        }

        mainStage.setScene(mainScene);
        mainStage.show();
    }
}

package GUI;

import Database.*;
import Entities.Position;
import GUI.Dialogs.ExceptionAlert;
import GUI.Dialogs.LoginDialog;
import GUI.Overviews.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicationGUI extends Application {
    public static DatabaseConnection databaseConnection;
    private static Thread refreshLists = null;

    private static Stage mainStage;
    private static Scene mainScene;

    private static Tile workersTile = new Tile(150);
    private static Tile teamsTile = new Tile(150);
    private static Tile projectsTile = new Tile(150);
    private static Tile positionsTile = new Tile(150);
    private static Tile holidaysTile = new Tile(150);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(refreshLists != null) {
                refreshLists.interrupt();
            }
        }));
        launch(args);
    }

    @Override
    public void stop(){
        if(refreshLists != null) {
            refreshLists.interrupt();
        }
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
        for(int i = 1; i <= 3; i++) {
            try{
                LoginDialog loginDialog = new LoginDialog();
                databaseConnection = new DatabaseConnection(loginDialog.getUsername(), loginDialog.getPassword());
                databaseConnection.getConnection();
                break;
            } catch (SQLException ex){
                if(i == 3){
                    new ExceptionAlert("Wrong login/password", "Closing the application").showAndWait();
                    System.exit(100);
                } else {
                    new ExceptionAlert("Wrong login/password", "Number of attempts: " + i).showAndWait();
                }
            }
        }

        refreshLists = new RefreshLists();
        refreshLists.start();

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

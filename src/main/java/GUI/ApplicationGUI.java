package GUI;

import Database.DatabaseConnection;
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
    public static String username;
    public static String password;

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

    public Scene mainGUI(){
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

    @Override
    public void start(Stage mainStage) {
        this.mainStage = mainStage;
        LoginDialog loginDialog = new LoginDialog();
        username = loginDialog.getUsername();
        password = loginDialog.getPassword();
        databaseConnection = new DatabaseConnection(loginDialog.getUsername(), loginDialog.getPassword());

        mainScene = mainGUI();
        mainStage.setResizable(false);

        workersTile.changeMainContent(new Scene(new Workers(mainStage, mainScene)), mainStage);
        teamsTile.changeMainContent(new Scene(new Teams(mainStage, mainScene)), mainStage);
        projectsTile.changeMainContent(new Scene(new Projects(mainStage, mainScene)), mainStage);
        positionsTile.changeMainContent(new Scene(new Positions(mainStage, mainScene)), mainStage);
        holidaysTile.changeMainContent(new Scene(new Holidays(mainStage, mainScene)), mainStage);

        mainStage.setScene(mainScene);
        mainStage.show();
    }
}

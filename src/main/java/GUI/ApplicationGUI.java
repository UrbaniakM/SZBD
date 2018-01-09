package GUI;

import Database.DatabaseConnection;
import GUI.Dialogs.LoginDialog;
import GUI.Overviews.MainContent;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ApplicationGUI extends Application {
    private BorderPane borderPane;
    private final Tile settingsButton = new Tile(300, 100, "Settings");
    private DatabaseConnection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception{
        connection.getConnection().close();
    }

    @Override
    public void start(Stage primaryStage) {
        LoginDialog loginDialog = new LoginDialog();
        connection = new DatabaseConnection(loginDialog.getUsername(), loginDialog.getPassword());
        borderPane = new BorderPane();

        final Scene scene = new Scene(new MainContent(connection.getConnection()),750,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

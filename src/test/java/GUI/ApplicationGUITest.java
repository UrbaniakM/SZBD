package GUI;

import Database.DatabaseConnection;
import GUI.Dialogs.LoginDialog;
import GUI.Overviews.Workers;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class ApplicationGUITest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginDialog loginDialog = new LoginDialog();
        try {
            Connection connection = new DatabaseConnection(loginDialog.getUsername(), loginDialog.getPassword()).getConnection();
        } catch (Exception ex) { }
        primaryStage.setScene(new Scene(new Workers()));
        primaryStage.show();
    }
}

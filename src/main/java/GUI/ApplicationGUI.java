package GUI;

import Database.DatabaseConnection;
import Features.WorkersDisplay;
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
        WorkersDisplay workersDisplay = new WorkersDisplay(connection.getConnection());
        borderPane = new BorderPane();
        // TOP
        HBox topPane = new HBox();
        EntityAvatar userAvatar = new EntityAvatar();
        userAvatar.setAlignment(Pos.CENTER_RIGHT);
        settingsButton.setAlignment(Pos.CENTER_LEFT);
        topPane.getChildren().addAll(settingsButton, userAvatar);
        topPane.setHgrow(userAvatar, Priority.ALWAYS);
        topPane.setStyle("-fx-background-color: f0f5fa;");
        //
        borderPane.setTop(topPane);
        borderPane.setCenter(new MainContent());

        final Scene scene = new Scene(borderPane,750,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

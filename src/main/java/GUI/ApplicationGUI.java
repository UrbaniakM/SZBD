package GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.html.parser.Entity;

public class ApplicationGUI extends Application {
    private BorderPane borderPane;
    private final Tile settingsButton = new Tile(300, 100, "Settings");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
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

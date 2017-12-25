package GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.text.html.parser.Entity;

public class ApplicationGUI extends Application {
    private BorderPane borderPane;
    private final MenuButton settingsButton = new MenuButton("Settings");

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
        //
        borderPane.setLeft(new Menu());
        borderPane.setTop(topPane);

        final Scene scene = new Scene(borderPane,750,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

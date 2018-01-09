package GUI;

import Database.DatabaseConnection;
import GUI.Dialogs.LoginDialog;
import GUI.Overviews.Workers;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ApplicationGUI extends Application {
    private DatabaseConnection connection;

    private static Stage mainStage;
    private static Scene mainScene;

    private static Tile workersTile = new Tile(150);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception{
        connection.getConnection().close();
    }

    public Scene mainGUI(){
        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 5, 5, 5));
        flowPane.setVgap(4);
        flowPane.setHgap(4);

        workersTile.setText("Workers");


        flowPane.getChildren().addAll(workersTile);

        return new Scene(flowPane, 750, 750);
    }

    @Override
    public void start(Stage mainStage) {
        this.mainStage = mainStage;
        LoginDialog loginDialog = new LoginDialog();
        connection = new DatabaseConnection(loginDialog.getUsername(), loginDialog.getPassword());
        mainScene = mainGUI();
        workersTile.changeMainContent(new Scene(new Workers(mainStage, mainScene, connection.getConnection())), mainStage);


        mainStage.setScene(mainScene);
        mainStage.show();
    }
}

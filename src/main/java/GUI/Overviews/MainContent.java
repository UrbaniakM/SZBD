package GUI.Overviews;

import GUI.Tile;
import GUI.Dialogs.Workers.AddWorkerDialog;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

import java.sql.Connection;

public class MainContent extends FlowPane {

    public MainContent(Connection connection){
        super();
        this.setPadding(new Insets(5, 0, 5, 0));
        this.setVgap(4);
        this.setHgap(4);
        this.setStyle("-fx-background-color: f0f5fa;");
        Tile workers = new Tile(150, "Workers");
        workers.changeMainContent(new AddWorkerDialog(),connection);
        this.getChildren().add(workers);
        this.getChildren().add(new Tile(150, "Teams"));
        this.getChildren().add(new Tile(150, "Projects"));
        this.getChildren().add(new Tile(150, "Summaries"));
    }
}

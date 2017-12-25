package GUI;

import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MainContent extends FlowPane {

    public MainContent(){
        super();
        this.setPadding(new Insets(5, 0, 5, 0));
        this.setVgap(4);
        this.setHgap(4);
        this.setStyle("-fx-background-color: f0f5fa;");
        this.getChildren().add(new Tile(150, "Workers"));
        this.getChildren().add(new Tile(150, "Teams"));
        this.getChildren().add(new Tile(150, "Projects"));
        this.getChildren().add(new Tile(150, "Summaries"));
    }
}

package GUI;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Menu extends VBox{
    private final MenuButton workersButton = new MenuButton("Workers");
    private final MenuButton teamsButton = new MenuButton("Teams");
    private final MenuButton projectsButton = new MenuButton("Projects");
    private final MenuButton summariesButton = new MenuButton("Summaries");

    public Menu(){
        this.getChildren().addAll(workersButton, teamsButton, projectsButton, summariesButton);
        workersButton.setAlignment(Pos.CENTER_LEFT);
        teamsButton.setAlignment(Pos.CENTER_LEFT);
        projectsButton.setAlignment(Pos.CENTER_LEFT);
        summariesButton.setAlignment(Pos.CENTER_LEFT);
    }
}

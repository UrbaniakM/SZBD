package GUI;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MenuButton extends HBox {
    public MenuButton(String name){
        Text text = new Text(name);
        this.getChildren().add(text);
        this.setMinHeight(100);
    }
}

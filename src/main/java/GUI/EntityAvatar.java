package GUI;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class EntityAvatar extends HBox{
    private final Circle circle = new Circle(50);
    public EntityAvatar(Image image){
        circle.setFill(new ImagePattern(image));
        this.getChildren().addAll(circle);
    }

    public EntityAvatar(){
        this.getChildren().addAll(circle);
    }
}

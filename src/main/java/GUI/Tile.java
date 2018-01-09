package GUI;

import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.sql.Connection;

public class Tile extends StackPane {
    public int size;

    public Tile(int size){
        super();
        this.size = size;

        Rectangle borders = new Rectangle(size-1, size-1);
        borders.setFill(Color.WHITE);
        borders.setArcHeight(10);
        borders.setArcWidth(10);
        borders.setStroke(Color.BLACK);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);

        borders.setEffect(dropShadow);

        this.getChildren().addAll(borders);
    }

    public void setText(String content){
        Text text = new Text(content);
        text.setFont(Font.font("Verdana",25));
        text.setWrappingWidth(size);
        text.setTextAlignment(TextAlignment.CENTER);

        this.getChildren().add(text);
    }


    public final void changeMainContent(Scene content, Stage mainStage){
        this.setOnMousePressed((event) -> {
            mainStage.setScene(content);
        });
    }
}

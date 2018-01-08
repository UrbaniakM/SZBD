package GUI;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.sql.Connection;

public class Tile extends StackPane {
    public Tile(int size){
        super();
        Rectangle borders = new Rectangle(size,size);
        borders.setFill(Paint.valueOf("DAE6F3"));
        borders.setArcHeight(25);
        borders.setArcWidth(25);

        this.getChildren().addAll(borders);
    }

    public Tile(int size, String content){
        super();
        Rectangle borders = new Rectangle(size,size);
        borders.setFill(Paint.valueOf("DAE6F3"));
        borders.setArcHeight(25);
        borders.setArcWidth(25);

        Text text = new Text(content);
        text.setFont(Font.font("Verdana",25));
        text.setWrappingWidth(size);
        text.setTextAlignment(TextAlignment.CENTER);

        this.getChildren().addAll(borders, text);
    }

    public Tile(int width, int height, String content){
        super();
        Rectangle borders = new Rectangle(width,height);
        borders.setFill(Paint.valueOf("DAE6F3"));
        borders.setArcHeight(25);
        borders.setArcWidth(25);

        Text text = new Text(content);
        text.setFont(Font.font("Verdana",25));
        text.setWrappingWidth(width);
        text.setTextAlignment(TextAlignment.CENTER);

        this.getChildren().addAll(borders, text);
    }

    public final void changeMainContent(AbstractDialog content, Connection connection){ // TODO: pop dialog zamiast setCenter
        this.setOnMousePressed((event) -> {
            content.popDialog(connection);
        });
    }
}

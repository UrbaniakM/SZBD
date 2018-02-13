package GUI.Dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class ExceptionAlert extends Alert {

    public ExceptionAlert(String title, String text){
        super(AlertType.ERROR, text);
        this.setTitle(title);
    }
}

package GUI.Dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DeleteAlert extends Alert{
    public DeleteAlert(){
        super(AlertType.CONFIRMATION);
        this.setTitle("Confirmation");
        this.setHeaderText(null);
        this.setContentText("Are you sure to delete?");
    }

    public boolean popDialog(){
        Optional<ButtonType> action = this.showAndWait();

        if(action.get() == ButtonType.OK){
            return true;
        }
        return false;
    }
}

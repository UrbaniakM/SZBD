package GUI.Dialogs.Positions;

import Database.PositionsModification;
import Entities.Position;
import GUI.Dialogs.AbstractDialog;
import GUI.Dialogs.ExceptionAlert;
import javafx.scene.control.ButtonType;

import java.sql.SQLException;
import java.util.Optional;

public class EditPositionDialog extends AbstractDialog {
    private ButtonType confirmButtonType;
    private Position positionAfterEdition;
    private Position positionBeforeEdition;

    // TODO WSZYSTKO

    public EditPositionDialog(Position position){

    }

    public Position popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            positionAfterEdition.setName(result.get().getName());
            positionAfterEdition.setWage(result.get().getWage());
            try{
                PositionsModification.editObject(positionBeforeEdition,positionAfterEdition);
            }catch (SQLException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with editing the position", "Position no longer in database.").showAndWait();
            }
            return positionAfterEdition;
        }
        return null;
    }

    private class Result {
        private String name;
        private int wage;

        public Result(String name, String wage) {
            this.name = name;
            this.wage = Integer.valueOf(wage);
        }

        public String getName() {
            return name;
        }

        public int getWage() {
            return wage;
        }
    }
}

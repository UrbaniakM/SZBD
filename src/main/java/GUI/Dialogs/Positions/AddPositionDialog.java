package GUI.Dialogs.Positions;

import Database.PositionsModification;
import Entities.Position;
import GUI.Dialogs.AbstractDialog;
import GUI.Dialogs.ExceptionAlert;
import javafx.scene.control.ButtonType;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AddPositionDialog extends AbstractDialog {
    private ButtonType confirmButtonType;

    // TODO WSZYSTKO

    public Position popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Position position = new Position();
            position.setName(result.get().getName());
            position.setWage(result.get().getWage());
            try {
                PositionsModification.addObject(position);
            } catch (SQLException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with adding new position", "Position with this name already in database.").showAndWait();
            }
            return position;
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

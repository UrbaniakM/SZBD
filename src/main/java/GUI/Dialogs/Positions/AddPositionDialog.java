package GUI.Dialogs.Positions;

import Database.PositionsModification;
import Entities.Position;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

import java.sql.Date;
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
            PositionsModification.addObject(position);
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

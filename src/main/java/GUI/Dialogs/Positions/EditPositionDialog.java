package GUI.Dialogs.Positions;

import Database.PositionsModification;
import Entities.Position;
import GUI.Dialogs.AbstractDialog;
import javafx.scene.control.ButtonType;

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
            PositionsModification.editObject(positionBeforeEdition,positionAfterEdition);
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

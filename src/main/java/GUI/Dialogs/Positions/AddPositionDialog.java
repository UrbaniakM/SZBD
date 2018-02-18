package GUI.Dialogs.Positions;

import Database.PositionsModification;
import Entities.Position;
import GUI.Dialogs.AbstractDialog;
import GUI.Dialogs.ExceptionAlert;
import GUI.TextFieldRestrictions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AddPositionDialog extends AbstractDialog {
    private ButtonType confirmButtonType;

    public AddPositionDialog(){
        super();
        this.setTitle("New position");
        confirmButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameTF = new TextField();
        nameTF.setPromptText("Name");
        TextField wageTF = new TextField();
        wageTF.setPromptText("Wage");

        TextFieldRestrictions.addIntegerRestriction(wageTF);

        TextFieldRestrictions.addTextLimiter(nameTF,32);
        TextFieldRestrictions.addTextLimiter(wageTF,6);

        Node loginButton = this.getDialogPane().lookupButton(confirmButtonType);
        loginButton.setDisable(true);
        loginButton.disableProperty().bind(
                nameTF.textProperty().isEmpty()
                        .or( wageTF.textProperty().isEmpty() )
        );


        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameTF, 1, 1);
        grid.add(new Label("Wage:"), 0, 2);
        grid.add(wageTF, 1, 2);

        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Result(nameTF.getText(), wageTF.getText());
            }
            return null;
        });
    }


    public Position popDialog(){
        Optional<Result> result = this.showAndWait();
        if (result.isPresent()) {
            Position position = new Position();
            position.setName(result.get().getName());
            position.setWage(result.get().getWage());
            try {
                PositionsModification.addObject(position);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                return null;
            } catch (IllegalArgumentException ex){
                new ExceptionAlert("Error with adding new position", "Position with this name already in database.").showAndWait();
                return null;
            }
            return position;
        }
        return null;
    }

    private class Result {
        private String name;
        private Integer wage;

        public Result(String name, String wage) {
            this.name = name;
            this.wage = Integer.valueOf(wage);
        }

        public String getName() {
            return name;
        }

        public Integer getWage() {
            return wage;
        }
    }
}

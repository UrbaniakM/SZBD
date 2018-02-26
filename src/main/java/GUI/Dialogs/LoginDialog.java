package GUI.Dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.Optional;

public class LoginDialog extends Dialog {
    private ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
    private ButtonType cancelButtonType = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

    private String username;
    private String password;
    /*final Action actionLogin = new AbstractAction("Login") {
        public void handle(ActionEvent ae) {
            Dialog d = (Dialog) ae.getSource();
            // Do the login here.
            d.hide();
        }
    };*/

    public LoginDialog(){
        super();
        this.setTitle("Log to database");
        initStyle(StageStyle.UNDECORATED);

        this.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = this.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        this.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            } else if(dialogButton == cancelButtonType){
                System.exit(0);
            }
            return null;
        });

        Optional<Pair<String, String>> result = this.showAndWait();

        result.ifPresent(usernamePassword -> {
            this.username = usernamePassword.getKey();
            this.password = usernamePassword.getValue();
        });
    }

    public String getPassword(){
        return password;
    }

    public String getUsername(){
        return username;
    }
}

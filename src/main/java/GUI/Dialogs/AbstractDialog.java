package GUI.Dialogs;

import javafx.scene.control.Dialog;

import java.sql.Connection;

public abstract class AbstractDialog extends Dialog{
    abstract public <T> T popDialog();
}

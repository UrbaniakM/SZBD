package GUI.Overviews;

import Database.TeamsModification;
import Database.WorkersModification;
import GUI.Dialogs.ExceptionAlert;
import javafx.collections.FXCollections;

import java.sql.SQLException;

public class RefreshLists extends Thread{

    @Override
    public void run() {
        super.run();
        while(!isInterrupted()){
            try {
                Workers.refreshTableView();
                Teams.refreshTableView();
                Holidays.refreshTableView();
                Positions.refreshTableView();
                Projects.refreshTableView();
                Thread.sleep(5000);
            } catch (SQLException | NullPointerException ex){
                new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
            } catch (InterruptedException ex){
                break;
            }
        }
    }
}

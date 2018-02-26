package GUI.Overviews;

import GUI.Dialogs.ExceptionAlert;
import javafx.application.Platform;

import java.sql.SQLException;

public class RefreshLists extends Thread{

    @Override
    public void run() {
        super.run();
        while(!isInterrupted()){
            try {
                Workers.refreshTableView();
                if(isInterrupted()){
                    break;
                }
                Teams.refreshTableView();
                if(isInterrupted()){
                    break;
                }
                Holidays.refreshTableView();
                if(isInterrupted()){
                    break;
                }
                Positions.refreshTableView();
                if(isInterrupted()){
                    break;
                }
                Projects.refreshTableView();
                Thread.sleep(1000);
            } catch (SQLException | NullPointerException ex){
                Platform.runLater(() -> {
                    new ExceptionAlert("Database error", "Problem with connection. Try again later.").showAndWait();
                });
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e){
                    break;
                }
            } catch (InterruptedException e){
                break;
            }

        }
    }
}

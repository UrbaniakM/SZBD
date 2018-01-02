package Features;

import Entities.Worker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkersDisplay {
    private List <Worker> data = new ArrayList<>();
    //private TableView<Worker> tableView = new TableView<>(); TODO: wrzucic to w klase w GUI, tutaj tylko backend
    private Connection connection;
    public WorkersDisplay(Connection connection){
        this.connection = connection;
    }

    public void importWorkers(){
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM workers");
            while(rs.next()) {
                //System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                Worker worker = new Worker();
                worker.setId(rs.getInt(1));
                worker.setName(rs.getString(2));
                worker.setLastName(rs.getString(3));
                worker.setPesel(rs.getInt(4));
                worker.setHireDate(rs.getDate(5));
                worker.setFireDate(rs.getDate(6));
                worker.setHoursPerWeek(rs.getInt(7));
                worker.setWage(rs.getFloat(8));// TODO: check if not null
                // TODO: zwracanie listy obiektow Worker, na którą wrzucane będą kolejne rekordy z bazy danych
            }
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return;
        }
    }

    public void addWorker(Worker worker){
        try {
            connection.createStatement().executeUpdate("INSERT INTO workers VALUES(" + worker.getId() + "," + worker.getName() + "," +
            worker.getLastName() + "," + worker.getPesel() + "," + worker.getHireDate() + "," + worker.getFireDate() + "," + worker.getHoursPerWeek() +
                    "," + worker.getWage() + ")");
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return;
        }
    }
}

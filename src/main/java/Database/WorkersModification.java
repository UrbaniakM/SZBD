package Database;

import Entities.Worker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkersModification {
    private Connection connection;

    public static List<Worker> importWorkers(Connection connection){
        List <Worker> data = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM workers");
            while(rs.next()) {
                Worker worker = new Worker();
                worker.setId(rs.getInt(1));
                worker.setName(rs.getString(2));
                worker.setLastName(rs.getString(3));
                worker.setPesel(rs.getInt(4));
                worker.setHireDate(rs.getDate(5));
                worker.setFireDate(rs.getDate(6));
                worker.setHoursPerWeek(rs.getInt(7));
                worker.setWage(rs.getFloat(8));
                worker.setIdEtatu(rs.getInt(9));
                data.add(worker);
            }
            return data;
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return null;
        }
    }

    public static void addWorker(Worker worker, Connection connection){
        try {
            connection.createStatement().executeUpdate("INSERT INTO workers VALUES(" + worker.getName() + "," +
            worker.getLastName() + "," + worker.getPesel() + "," + worker.getHireDate() + "," + worker.getFireDate() + "," + worker.getHoursPerWeek() +
                    "," + worker.getWage() + ")"); // TODO: "niewystarczajaca liczba wartosci", INNY FORMAT (potrzebne znaki ' ' w stringach)
            // TODO: jesli juz istnieje, na nowo hire zamiast dodawanie - sprawdzanie czy juz jest w tabeli
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return;
        }
    }

    public static void editWorker(Worker previousWorker, Worker newWorker, Connection connection){
        // TODO
    }
}

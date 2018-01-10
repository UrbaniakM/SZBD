package Database;

import Entities.Worker;

import java.sql.*;
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
                worker.setPesel(rs.getString(4));
                worker.setHireDate(rs.getDate(5));
                worker.setFireDate(rs.getDate(6));
                worker.setHoursPerWeek(rs.getInt(7));
                worker.setWage(rs.getInt(8));
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
            String sqlStatement = "INSERT INTO workers(imie, nazwisko, pesel, data_zatrudnienia, wymiar_godzin, pensja) VALUES " +
                    "(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,worker.getName());
            preparedStatement.setString(2,worker.getLastName());
            preparedStatement.setString(3,worker.getPesel());
            preparedStatement.setDate(4,worker.getHireDate());
            preparedStatement.setInt(5,worker.getHoursPerWeek());
            preparedStatement.setInt(6,worker.getWage());
            preparedStatement.executeUpdate();
            // TODO: jesli juz istnieje, na nowo hire zamiast dodawanie - sprawdzanie czy juz jest w tabeli
        } catch (SQLException ex){
            System.err.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return;
        }
    }

    public static void editWorker(Worker previousWorker, Worker newWorker, Connection connection){
        try {
            ResultSet selectStatement = connection.createStatement().executeQuery(
                    "SELECT pesel FROM workers WHERE pesel='" + previousWorker.getPesel() + "'"
            );
            if(selectStatement.next()){
                connection.createStatement().executeQuery("DELETE FROM workers WHERE pesel='" + previousWorker.getPesel() + "'");
                String insertStatement = "INSERT INTO workers(id_pracownika,imie, nazwisko, pesel, data_zatrudnienia, wymiar_godzin, pensja) VALUES " +
                        "(?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
                preparedStatement.setInt(1, previousWorker.getId());
                preparedStatement.setString(2,newWorker.getName());
                preparedStatement.setString(3,newWorker.getLastName());
                preparedStatement.setString(4,newWorker.getPesel());
                preparedStatement.setDate(5,newWorker.getHireDate());
                preparedStatement.setInt(6,newWorker.getHoursPerWeek());
                preparedStatement.setInt(7,newWorker.getWage());
                preparedStatement.executeUpdate();
            } else {
                System.err.println("Worker no longer in database. Data loss possible");
            }

        } catch (SQLException ex){
            System.err.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return;
        }
    }
}

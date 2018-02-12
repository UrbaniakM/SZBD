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
                worker.setPesel(rs.getString(1));
                worker.setName(rs.getString(2));
                worker.setLastName(rs.getString(3));
                worker.setHireDate(rs.getDate(4));
                worker.setBonus(rs.getInt(5));
                worker.setPositionName(rs.getString(6));
                worker.setTeamName(rs.getString(7));
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
            String sqlStatement = "INSERT INTO workers(imie, nazwisko, pesel, data_zatrudnienia, premia, nazwa_etatu, nazwa_zespolu) VALUES " +
                    "(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,worker.getName());
            preparedStatement.setString(2,worker.getLastName());
            preparedStatement.setString(3,worker.getPesel());
            preparedStatement.setDate(4,worker.getHireDate());
            preparedStatement.setInt(5,worker.getBonus());
            preparedStatement.setString(6,worker.getPositionName());
            preparedStatement.setString(7,worker.getTeamName());
            preparedStatement.executeUpdate();
            // TODO: sprawdzanie, czy jest w tabeli juz
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
                String insertStatement = "UPDATE workers SET imie = ?,  nazwisko = ?,  pesel = ?, data_zatrudnienia = ?, premia = ?, " +
                        "nazwa_etatu = ?, nazwa_zespolu = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);
                preparedStatement.setString(1,newWorker.getName());
                preparedStatement.setString(2,newWorker.getLastName());
                preparedStatement.setString(3,newWorker.getPesel());
                preparedStatement.setDate(4,newWorker.getHireDate());
                // TODO: if bonus == null the setNull
                preparedStatement.setInt(5,newWorker.getBonus());
                preparedStatement.setString(6,newWorker.getPositionName());
                preparedStatement.setString(7,newWorker.getTeamName());
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

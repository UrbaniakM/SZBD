package Database;

import Entities.Worker;
import GUI.ApplicationGUI;
import GUI.Dialogs.ExceptionAlert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkersModification {
    public static List<Worker> importObject(){
        List <Worker> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM workers");
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
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Worker worker) throws SQLException, IllegalArgumentException { // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM workers WHERE pesel='" + worker.getPesel() + "'"
            );
            if (selectStatement.next()) {
                throw new IllegalArgumentException("Worker with this PESEL already in database.");
            }
            String sqlStatement = "INSERT INTO workers(imie, nazwisko, pesel, data_zatrudnienia, premia, nazwa_etatu, nazwa_zespolu) VALUES " +
                    "(?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, worker.getName());
            preparedStatement.setString(2, worker.getLastName());
            preparedStatement.setString(3, worker.getPesel());
            preparedStatement.setDate(4, worker.getHireDate());
            preparedStatement.setInt(5, worker.getBonus());
            preparedStatement.setString(6, worker.getPositionName());
            preparedStatement.setString(7, worker.getTeamName());
            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalArgumentException ex) {
            throw ex;
        }finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
            try { selectStatement.close();; } catch (Exception ex) { };
        }
    }

    public static void editObject(Worker previousWorker, Worker newWorker){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT pesel FROM workers WHERE pesel='" + previousWorker.getPesel() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE workers SET imie = ?,  nazwisko = ?,  pesel = ?, data_zatrudnienia = ?, premia = ?, " +
                        "nazwa_etatu = ?, nazwa_zespolu = ? WHERE pesel = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newWorker.getName());
                preparedStatement.setString(2,newWorker.getLastName());
                preparedStatement.setString(3,newWorker.getPesel());
                preparedStatement.setDate(4,newWorker.getHireDate());
                // TODO: if bonus == null the setNull
                preparedStatement.setInt(5,newWorker.getBonus());
                preparedStatement.setString(6,newWorker.getPositionName());
                preparedStatement.setString(7,newWorker.getTeamName());
                preparedStatement.setString(8,previousWorker.getPesel());
                preparedStatement.executeUpdate();
            } else { //  TODO - DIALOG + THROW EXCEPTION?
                System.err.println("Worker no longer in database. Data loss possible");
            }

        } catch (SQLException ex){
            System.err.println("Statement execution failed! Check output console");
            ex.printStackTrace();
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
        }
    }

    // TODO: DELETE OBJECT
}

package Database;

import Entities.Team;
import Entities.Worker;
import GUI.ApplicationGUI;
import GUI.Dialogs.ExceptionAlert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkersModification {
    public static List<Worker> importObject() throws SQLException, NullPointerException{
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
        } catch (SQLException | NullPointerException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.getStatement().close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Worker worker) throws SQLException, IllegalArgumentException, NullPointerException {
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
            else {
                String sqlStatement = "INSERT INTO workers(imie, nazwisko, pesel, data_zatrudnienia, premia, nazwa_etatu, nazwa_zespolu) VALUES " +
                        "(?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, worker.getName());
                preparedStatement.setString(2, worker.getLastName());
                preparedStatement.setString(3, worker.getPesel());
                preparedStatement.setDate(4, worker.getHireDate());
                preparedStatement.setObject(5, worker.getBonus());
                preparedStatement.setString(6, worker.getPositionName());
                preparedStatement.setString(7, worker.getTeamName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        }finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close();; } catch (Exception ex) { };
        }
    }

    public static void editObject(Worker previousWorker, Worker newWorker) throws SQLException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try { // TODO: check if not already in database
            // TODO: edit in other tables
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
                preparedStatement.setObject(5,newWorker.getBonus());
                preparedStatement.setString(6,newWorker.getPositionName());
                preparedStatement.setString(7,newWorker.getTeamName());
                preparedStatement.setString(8,previousWorker.getPesel());
                preparedStatement.executeUpdate();
            } else {
                throw new IllegalArgumentException("Worker no longer in database.");
            }

        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
        }
    }


    public static void deleteObject(Worker worker) throws SQLException, IllegalArgumentException, NullPointerException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        ResultSet inTeamDatabase = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT workers.* FROM workers WHERE pesel='" + worker.getPesel() + "'"
            );
            inTeamDatabase = connection.createStatement().executeQuery(
                    "SELECT * FROM teams WHERE pesel_leader='" + worker.getPesel() + "'"
            );
            if(inTeamDatabase.next()){
                throw new SQLDataException("Worker in teams table.");  // TODO: assign null leader
            }
            else if(selectStatement.next()){
                HolidaysModification.deleteObject(worker); // delete holidays assigned to this worker
                selectStatement.deleteRow();
            } else {
                throw new IllegalArgumentException("Worker no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { inTeamDatabase.getStatement().close(); } catch (Exception ex) { };
            try { inTeamDatabase.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }
    }

    public static int countObjects() throws SQLException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT COUNT(*) FROM workers"
            );
            if(selectStatement.next()){
                return selectStatement.getInt(1);
            }
            return 0;
        } catch (SQLException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }
    }
}

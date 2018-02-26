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
            //rs = connection.createStatement().executeQuery("SELECT * FROM workers");
            rs = connection.createStatement().executeQuery("SELECT workers.id, pesel, imie, nazwisko, data_zatrudnienia," +
                    "premia, id_position, id_team, positions.nazwa, teams.nazwa FROM workers INNER JOIN positions ON id_position = positions.id " +
                    "LEFT JOIN teams ON id_team = teams.id"); // TODO: rozbij to na getTeamInfo oraz getPositionInfo (arg: Integer id)
            while(rs.next()) {
                Worker worker = new Worker();
                worker.setId(rs.getInt(1));
                worker.setPesel(rs.getString(2));
                worker.setName(rs.getString(3));
                worker.setLastName(rs.getString(4));
                worker.setHireDate(rs.getDate(5));
                worker.setBonus(rs.getInt(6));
                worker.setPositionId(rs.getInt(7));
                worker.setTeamId(rs.getInt(8));
                worker.setPositionName(rs.getString(9));
                worker.setTeamName(rs.getString(10));
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

    public static Worker importObject(Worker fetchWorker) throws SQLException, NullPointerException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT workers.id, pesel, imie, nazwisko, data_zatrudnienia, " +
                    "premia, id_position, id_team, positions.nazwa, teams.nazwa FROM workers WHERE pesel='" + fetchWorker.getPesel() +
                    "' INNER JOIN positions ON id_position = positions.id " + "LEFT JOIN teams ON id_team = teams.id"
            );
            if(rs.next()) {
                Worker worker = new Worker();
                worker.setId(rs.getInt(1));
                worker.setPesel(rs.getString(2));
                worker.setName(rs.getString(3));
                worker.setLastName(rs.getString(4));
                worker.setHireDate(rs.getDate(5));
                worker.setBonus(rs.getInt(6));
                worker.setPositionId(rs.getInt(7));
                worker.setTeamId(rs.getInt(8));
                return worker;
            } else {
                throw new IllegalArgumentException("Worker no longer in database.");
            }
        } catch (SQLException | NullPointerException | IllegalArgumentException ex) {
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
                String sqlStatement = "INSERT INTO workers(imie, nazwisko, pesel, data_zatrudnienia, premia, id_position, id_team) VALUES " +
                        "(?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, worker.getName());
                preparedStatement.setString(2, worker.getLastName());
                preparedStatement.setString(3, worker.getPesel());
                preparedStatement.setDate(4, worker.getHireDate());
                preparedStatement.setObject(5, worker.getBonus());
                preparedStatement.setInt(6, worker.getPositionId());
                preparedStatement.setObject(7, worker.getTeamId());
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
        try {
            // TODO: make sure UNIQUE PESEL
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT id FROM workers WHERE id='" + newWorker.getId() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE workers SET imie = ?,  nazwisko = ?,  pesel = ?, data_zatrudnienia = ?, premia = ?, " +
                        "id_position = ?, id_team = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newWorker.getName());
                preparedStatement.setString(2,newWorker.getLastName());
                preparedStatement.setString(3,newWorker.getPesel());
                preparedStatement.setDate(4,newWorker.getHireDate());
                preparedStatement.setObject(5,newWorker.getBonus());
                preparedStatement.setInt(6,newWorker.getPositionId());
                preparedStatement.setObject(7,newWorker.getTeamId());
                preparedStatement.setInt(8,newWorker.getId());
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
                    "SELECT workers.* FROM workers WHERE id='" + worker.getId() + "'"
            );
            inTeamDatabase = connection.createStatement().executeQuery(
                    "SELECT * FROM teams WHERE id_leader='" + worker.getId() + "'"
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

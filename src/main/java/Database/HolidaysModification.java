package Database;

import Entities.Holiday;
import Entities.Worker;
import GUI.ApplicationGUI;
import oracle.net.jdbc.nl.NLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HolidaysModification {
    public static List<Holiday> importObject() throws SQLException, NullPointerException{
        List <Holiday> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM holidays");
            while(rs.next()) {
                Holiday holiday = new Holiday();
                holiday.setId(rs.getInt(1));
                holiday.setWorkerId(rs.getInt(2));
                holiday.setBeginDate(rs.getDate(3));
                holiday.setEndDate(rs.getDate(4));
                data.add(holiday);
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

    public static Holiday importObject(Holiday fetchHoliday) throws SQLException, NullPointerException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM holidays WHERE id_worker='" + fetchHoliday.getWorkerId() +
                    "' AND czas_rozpoczecia='" + fetchHoliday.getBeginDate() + "' AND czas_zakonczenia='" + fetchHoliday.getEndDate() + "'"
            );
            if(rs.next()) {
                Holiday holiday = new Holiday();
                holiday.setId(rs.getInt(1));
                holiday.setWorkerId(rs.getInt(2));
                holiday.setBeginDate(rs.getDate(3));
                holiday.setEndDate(rs.getDate(4));
                return holiday;
            } else {
                throw new IllegalArgumentException("Holiday no longer in database.");
            }
        } catch (SQLException | NullPointerException | IllegalArgumentException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.getStatement().close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Holiday holiday) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try { // TODO: already in database
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM holidays WHERE id_worker='" + holiday.getWorkerId() + "' AND czas_rozpoczecia='" + holiday.getBeginDate()+"'"
            );
            if (selectStatement.next()) {
                throw new IllegalArgumentException("Holiday already in database.");
            }
            else {
                String sqlStatement = "INSERT INTO holidays(id_worker, czas_rozpoczecia, czas_zakonczenia) VALUES " +
                        "(?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setInt(1, holiday.getWorkerId());
                preparedStatement.setDate(2, holiday.getBeginDate());
                preparedStatement.setDate(3, holiday.getEndDate());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close();; } catch (Exception ex) { };
        }
    }

    public static void editObject(Holiday previousHoliday, Holiday newHoliday) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try { // TODO: check if not already in database
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT id FROM holidays WHERE id='" + previousHoliday.getId() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE holidays SET id_worker = ?, czas_rozpoczecia = ?, czas_zakonczenia = ?" + // TODO: zakoNczenia, nie zakoczenia
                        "WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setInt(1,newHoliday.getWorkerId());
                preparedStatement.setDate(2,newHoliday.getBeginDate());
                preparedStatement.setDate(3,newHoliday.getEndDate());
                preparedStatement.setInt(4,previousHoliday.getId());
                preparedStatement.executeUpdate();
            } else {
                throw new IllegalArgumentException("Holiday no longer in database.");
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

    public static void deleteObject(Holiday holiday) throws SQLException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT id FROM holidays WHERE id='" + holiday.getId() + "'"
            );
            if(selectStatement.next()){
                selectStatement.deleteRow();
            } else {
                throw new IllegalArgumentException("Holiday no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }

    }

    public static void deleteObject(Worker worker) throws SQLException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT holidays.* FROM holidays WHERE id_worker='" + worker.getId() + "'"
            );
            while(selectStatement.next()){
                selectStatement.deleteRow();
            }
        } catch (SQLException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }

    }
}

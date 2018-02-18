package Database;

import Entities.Holiday;
import GUI.ApplicationGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HolidaysModification {
    public static List<Holiday> importObject() throws SQLException{
        List <Holiday> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM holidays");
            while(rs.next()) {
                Holiday holiday = new Holiday();
                holiday.setId(rs.getInt(1));
                holiday.setPesel(rs.getString(2));
                holiday.setBeginDate(rs.getDate(3));
                holiday.setEndDate(rs.getDate(4));
                data.add(holiday);
            }
            return data;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Holiday holiday) throws SQLException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery( // TODO: createStatement close
                    "SELECT * FROM holidays WHERE pesel='" + holiday.getPesel() + "' AND czas_rozpoczecia='" + holiday.getBeginDate()+"'"
            );
            if (selectStatement.next()) {
                throw new IllegalArgumentException("Holiday already in database.");
            }
            else {
                String sqlStatement = "INSERT INTO holidays(pesel, czas_rozpoczecia, czas_zakoczenia) VALUES " + // TODO: zakoNczenia, nie zakoczenia
                        "(?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, holiday.getPesel());
                preparedStatement.setDate(2, holiday.getBeginDate());
                preparedStatement.setDate(3, holiday.getEndDate());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IllegalArgumentException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close();; } catch (Exception ex) { };
        }
    }

    public static void editObject(Holiday previousHoliday, Holiday newHoliday) throws SQLException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try { // TODO: check if not already in database
            selectStatement = connection.createStatement().executeQuery( // TODO: createStatement close
                    "SELECT id_num FROM holidays WHERE id_num='" + previousHoliday.getId() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE holidays SET pesel = ?, czas_rozpoczecia = ?, czas_zakoczenia = ?" + // TODO: zakoNczenia, nie zakoczenia
                        "WHERE id_num = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newHoliday.getPesel());
                preparedStatement.setDate(2,newHoliday.getBeginDate());
                preparedStatement.setDate(3,newHoliday.getEndDate());
                preparedStatement.setInt(4,previousHoliday.getId());
                preparedStatement.executeUpdate();
            } else {
                throw new IllegalArgumentException("Holiday no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException ex){
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
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery( // TODO: createStatement close
                    "SELECT id_num FROM holidays WHERE id_num='" + holiday.getId() + "'"
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
}

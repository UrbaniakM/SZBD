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
    public static List<Holiday> importObject(){
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
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return null;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Holiday holiday){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sqlStatement = "INSERT INTO holidays(pesel, czas_rozpoczecia, czas_zakonczenia) VALUES " +
                    "(?,?,?)";
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,holiday.getPesel());
            preparedStatement.setDate(2,holiday.getBeginDate());
            preparedStatement.setDate(3,holiday.getEndDate());
            preparedStatement.executeUpdate();
            // TODO: sprawdzanie, czy jest w tabeli juz
        } catch (SQLException ex){
            System.err.println("Statement execution failed! Check output console");
            ex.printStackTrace();
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
        }
    }

    public static void editObject(Holiday previousHoliday, Holiday newHoliday){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT id_num FROM holidays WHERE id_num='" + previousHoliday.getPesel() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE holidays SET pesel = ?, czas_rozpoczecia = ?, czas_zakonczenia = ?" +
                        "WHERE id_num = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newHoliday.getPesel());
                preparedStatement.setDate(2,newHoliday.getBeginDate());
                preparedStatement.setDate(3,newHoliday.getEndDate());
                preparedStatement.setInt(4,previousHoliday.getId());
                preparedStatement.executeUpdate();
            } else {
                System.err.println("Holiday no longer in database. Data loss possible");
            }
        } catch (SQLException ex){  //  TODO - DIALOG
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

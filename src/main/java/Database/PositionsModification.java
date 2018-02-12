package Database;

import Entities.Position;
import GUI.ApplicationGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PositionsModification {
    public static List<Position> importObject(){
        List <Position> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM positions");
            while(rs.next()) {
                Position position = new Position();
                position.setName(rs.getString(1));
                position.setWage(rs.getInt(2));
                data.add(position);
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

    public static void addObject(Position position){
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sqlStatement = "INSERT INTO positions(nazwa, pensja) VALUES " +
                    "(?,?)";
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,position.getName());
            preparedStatement.setInt(2,position.getWage());
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

    public static void editObject(Position previousPosition, Position newPosition){
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT nazwa FROM holidays WHERE nazwa='" + previousPosition.getName() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE positions SET nazwa = ?, pensja = ? WHERE nazwa = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newPosition.getName());
                preparedStatement.setInt(2,newPosition.getWage());
                preparedStatement.setString(3,previousPosition.getName());
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
}

package Database;

import Entities.Position;
import GUI.ApplicationGUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PositionsModification {
    public static List<Position> importObject() throws SQLException, NullPointerException{
        List <Position> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM positions");
            while(rs.next()) {
                Position position = new Position();
                position.setId(rs.getInt(1));
                position.setName(rs.getString(2));
                position.setWage(rs.getInt(3));
                data.add(position);
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

    public static Position importObject(Position fetchPosition) throws SQLException, NullPointerException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM positions WHERE nazwa='" + fetchPosition.getName() + "'");
            if(rs.next()) {
                Position position = new Position();
                position.setId(rs.getInt(1));
                position.setName(rs.getString(2));
                position.setWage(rs.getInt(3));
                return position;
            } else {
                throw new IllegalArgumentException("Position no longer in database.");
            }
        } catch (SQLException | NullPointerException | IllegalArgumentException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.getStatement().close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Position position) throws SQLException, IllegalArgumentException, NullPointerException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM positions WHERE nazwa='" + position.getName() + "'"
            );
            if (selectStatement.next()) {
                throw new IllegalArgumentException("Position with this name already in database.");
            }
            else {
                String sqlStatement = "INSERT INTO positions(nazwa, pensja) VALUES " +
                        "(?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, position.getName());
                preparedStatement.setInt(2, position.getWage());
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

    public static void editObject(Position previousPosition, Position newPosition) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            // TODO: check if name not already in database
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT id FROM positions WHERE id='" + previousPosition.getId() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE positions SET nazwa = ?, pensja = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newPosition.getName());
                preparedStatement.setInt(2,newPosition.getWage());
                preparedStatement.setInt(3,newPosition.getId());
                preparedStatement.executeUpdate();
            } else {
                throw new IllegalArgumentException("Position no longer in database.");
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

    public static void deleteObject(Position position) throws SQLException, IllegalArgumentException, NullPointerException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        ResultSet inAnotherDatabase = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT positions.* FROM positions WHERE id='" + position.getId() + "'"
            );
            inAnotherDatabase = connection.createStatement().executeQuery(
              "SELECT * FROM workers WHERE id_position='" + position.getId() + "'"
            );
            if(inAnotherDatabase.next()){
                throw new SQLDataException("Position in workers table.");
            }
            else if(selectStatement.next()){
                selectStatement.deleteRow();
            } else {
                throw new IllegalArgumentException("Position no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { inAnotherDatabase.getStatement().close(); } catch (Exception ex) { };
            try { inAnotherDatabase.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }
    }

    public static int countObjects() throws SQLException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT COUNT(*) FROM positions"
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

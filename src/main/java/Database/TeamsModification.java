package Database;

import Entities.Team;
import GUI.ApplicationGUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamsModification {
    public static List<Team> importObject() throws SQLException, NullPointerException{
        List <Team> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM teams");
            while(rs.next()) {
                Team team = new Team();
                team.setName(rs.getString(1));
                team.setCreationDate(rs.getDate(2));
                team.setLeaderPesel(rs.getString(3));
                data.add(team);
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

    public static void addObject(Team team) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM teams WHERE nazwa='" + team.getName() + "'"
            );
            if (selectStatement.next()) {
                throw new IllegalArgumentException("Project with this name already in database.");
            }
            else {
                String sqlStatement = "INSERT INTO teams(nazwa, data_utworzenia, pesel_leader) VALUES " +
                        "(?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, team.getName());
                preparedStatement.setDate(2, team.getCreationDate());
                preparedStatement.setObject(3, team.getLeaderPesel());
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

    public static void editObject(Team previousTeam, Team newTeam) throws SQLException, IllegalArgumentException, NullPointerException{ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM teams WHERE nazwa='" + previousTeam.getName() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE teams SET nazwa = ?, data_utworzenia = ?, pesel_leader = ?" +
                        "WHERE nazwa = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newTeam.getName());
                preparedStatement.setDate(2,newTeam.getCreationDate());
                preparedStatement.setString(3,newTeam.getLeaderPesel());
                preparedStatement.setString(4,previousTeam.getName());
                preparedStatement.executeUpdate();
            } else {
                throw new IllegalArgumentException("Team no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
        }
    }

    public static void deleteObject(Team team) throws SQLException, IllegalArgumentException, NullPointerException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        ResultSet inProjectDatabase = null;
        ResultSet inWorkerDabase = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT teams.* FROM teams WHERE nazwa='" + team.getName() + "'"
            );
            inProjectDatabase = connection.createStatement().executeQuery(
                    "SELECT * FROM projects WHERE nazwa_zespolu='" + team.getName() + "'"
            );
            inWorkerDabase = connection.createStatement().executeQuery(
                    "SELECT * FROM workers WHERE nazwa_zespolu='" + team.getName() + "'"
            );
            if(inProjectDatabase.next()){
                throw new SQLDataException("Team in projects table.");
            } else if(inWorkerDabase.next()){
                throw new SQLDataException("Team in workers table.");
            } else if(selectStatement.next()){
                selectStatement.deleteRow();
            } else {
                throw new IllegalArgumentException("Position no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { inProjectDatabase.getStatement().close(); } catch (Exception ex) { };
            try { inProjectDatabase.close(); }  catch (Exception ex) { };
            try { inWorkerDabase.getStatement().close(); } catch (Exception ex) { };
            try { inWorkerDabase.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }
    }
}

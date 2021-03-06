package Database;

import Entities.Team;
import Entities.Worker;
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
            rs = connection.createStatement().executeQuery("SELECT teams.id, teams.nazwa, data_utworzenia, id_leader, workers.pesel FROM teams " +
                    "LEFT JOIN workers ON id_leader=workers.id");
            while(rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt(1));
                team.setName(rs.getString(2));
                team.setCreationDate(rs.getDate(3));
                team.setLeaderId(rs.getInt(4));
                team.setLeaderPesel(rs.getString(5));
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

    public static Team importObject(Team fetchTeam) throws SQLException, NullPointerException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM teams WHERE nazwa='" + fetchTeam.getName() + "'");
            if(rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt(1));
                team.setName(rs.getString(2));
                team.setCreationDate(rs.getDate(3));
                team.setLeaderId(rs.getInt(4));
                return team;
            } else {
                throw new IllegalArgumentException("Team no longer in database.");
            }
        } catch (SQLException | NullPointerException | IllegalArgumentException ex) {
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
                String sqlStatement = "INSERT INTO teams(nazwa, data_utworzenia, id_leader) VALUES " +
                        "(?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, team.getName());
                preparedStatement.setDate(2, team.getCreationDate());
                preparedStatement.setObject(3, team.getLeaderId());
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

    public static void editObject(Team previousTeam, Team newTeam) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        ResultSet uniqueName;
        PreparedStatement preparedStatement = null;
        try {
            uniqueName = connection.createStatement().executeQuery("SELECT id FROM teams WHERE nazwa='" + newTeam.getName() + "'");
            if(uniqueName.next()){
                Integer id = uniqueName.getInt(1);
                if(id != previousTeam.getId()){
                    throw new IllegalArgumentException("Another team with this name");
                }
            }
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM teams WHERE id='" + newTeam.getId() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE teams SET nazwa = ?, data_utworzenia = ?, id_leader = ?" +
                        "WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newTeam.getName());
                preparedStatement.setDate(2,newTeam.getCreationDate());
                preparedStatement.setObject(3,newTeam.getLeaderId());
                preparedStatement.setInt(4,newTeam.getId());
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

    public static void setNullLeader(Worker worker) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT id_leader FROM teams WHERE id_leader='" + worker.getId() + "'"
            );
            while(selectStatement.next()){
                selectStatement.updateObject("id_leader",null);
                selectStatement.updateRow();
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }
    }

    public static void deleteObject(Team team) throws SQLException, IllegalArgumentException, NullPointerException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        ResultSet inProjectDatabase = null;
        ResultSet inWorkerDabase = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT teams.* FROM teams WHERE id='" + team.getId() + "'"
            );
            inProjectDatabase = connection.createStatement().executeQuery(
                    "SELECT * FROM projects WHERE id_team='" + team.getId() + "'"
            );
            inWorkerDabase = connection.createStatement().executeQuery(
                    "SELECT * FROM workers WHERE id_team='" + team.getId() + "'"
            );
            if(inProjectDatabase.next()){
                throw new SQLDataException("Team in projects table.");
            } else if(inWorkerDabase.next()){
                WorkersModification.setNullTeam(team);
            }
            if(selectStatement.next()){
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

    public static int countObjects() throws SQLException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT COUNT(*) FROM teams"
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

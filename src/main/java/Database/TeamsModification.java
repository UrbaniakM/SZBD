package Database;

import Entities.Team;
import GUI.ApplicationGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamsModification {
    public static List<Team> importObject(){
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
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return null;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Team team){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sqlStatement = "INSERT INTO teams(nazwa, data_utworzenia, pesel_leader) VALUES " +
                    "(?,?,?)";
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,team.getName());
            preparedStatement.setDate(2,team.getCreationDate());
            preparedStatement.setString(3,team.getLeaderPesel());
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

    public static void editObject(Team previousTeam, Team newTeam){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT nazwa FROM teams WHERE nazwa='" + previousTeam.getName() + "'"
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
                System.err.println("Team no longer in database. Data loss possible");
            }
        } catch (SQLException ex){  //  TODO - DIALOG + THROW EXCEPTION?
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

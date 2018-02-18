package Database;

import Entities.Project;
import GUI.ApplicationGUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsModification {
    public static List<Project> importObject() throws SQLException, NullPointerException{
        List <Project> data = new ArrayList<>();
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM projects");
            while(rs.next()) {
                Project project = new Project();
                project.setName(rs.getString(1));
                project.setBeginDate(rs.getDate(2));
                project.setEndDate(rs.getDate(3));
                project.setTeamName(rs.getString(4));
                data.add(project);
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

    public static void addObject(Project project) throws SQLException, IllegalArgumentException, NullPointerException{ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT * FROM projects WHERE nazwa='" + project.getName() + "'"
            );
            if (selectStatement.next()) {
                throw new IllegalArgumentException("Project with this name already in database.");
            }
            else {
                String sqlStatement = "INSERT INTO projects(nazwa, data_rozpoczecia, data_zakoczenia, nazwa_zespolu) VALUES " +
                        "(?,?,?,?)"; // TODO: data_zakoNczenia, nie data_zakoczenia
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, project.getName());
                preparedStatement.setDate(2, project.getBeginDate());
                preparedStatement.setDate(3, project.getEndDate());
                preparedStatement.setString(4, project.getTeamName());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | IllegalArgumentException | NullPointerException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { preparedStatement.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close();; } catch (Exception ex) { };
        }
    }

    public static void editObject(Project previousProject, Project newProject) throws SQLException, IllegalArgumentException, NullPointerException{ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try { // TODO: check if not already in database
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT nazwa FROM projects WHERE nazwa='" + previousProject.getName() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE projects SET nazwa = ?, data_rozpoczecia = ?, data_zakoczenia = ?, nazwa_zespolu = ?" +
                        "WHERE nazwa = ?"; // TODO: data_zakoNczenia, nie data_zakoczenia
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newProject.getName());
                preparedStatement.setDate(2,newProject.getBeginDate());
                preparedStatement.setDate(3,newProject.getEndDate());
                preparedStatement.setString(4,newProject.getTeamName());
                preparedStatement.setString(5,previousProject.getName());
                preparedStatement.executeUpdate();
            } else {
                throw new IllegalArgumentException("Project no longer in database.");
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

    // TODO: DELETE OBJECT
}

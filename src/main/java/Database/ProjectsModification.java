package Database;

import Entities.Project;
import GUI.ApplicationGUI;

import java.sql.*;
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
                project.setId(rs.getInt(1));
                project.setName(rs.getString(2));
                project.setBeginDate(rs.getDate(3));
                project.setEndDate(rs.getDate(4));
                project.setTeamId(rs.getInt(5));
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

    public static Project importObject(Project fetchProject) throws SQLException, NullPointerException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet rs = null;
        try {
            rs = connection.createStatement().executeQuery("SELECT * FROM projects WHERE nazwa='" + fetchProject.getName() + "'");
            if(rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt(1));
                project.setName(rs.getString(2));
                project.setBeginDate(rs.getDate(3));
                project.setEndDate(rs.getDate(4));
                project.setTeamId(rs.getInt(5));
                return project;
            } else {
                throw new IllegalArgumentException("Project no longer in database.");
            }
        } catch (SQLException | NullPointerException | IllegalArgumentException ex) {
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.getStatement().close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Project project) throws SQLException, IllegalArgumentException, NullPointerException{
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
                String sqlStatement = "INSERT INTO projects(nazwa, data_rozpoczecia, data_zakonczenia, id_team) VALUES " +
                        "(?,?,?,?)"; // TODO: data_zakoNczenia, nie data_zakoczenia
                preparedStatement = connection.prepareStatement(sqlStatement);
                preparedStatement.setString(1, project.getName());
                preparedStatement.setDate(2, project.getBeginDate());
                preparedStatement.setDate(3, project.getEndDate());
                preparedStatement.setInt(4, project.getTeamId());
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

    public static void editObject(Project previousProject, Project newProject) throws SQLException, IllegalArgumentException, NullPointerException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            // TODO: check if not already in database
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT nazwa FROM projects WHERE nazwa='" + previousProject.getName() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE projects SET nazwa = ?, data_rozpoczecia = ?, data_zakonczenia = ?, id_team = ?" +
                        "WHERE id = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newProject.getName());
                preparedStatement.setDate(2,newProject.getBeginDate());
                preparedStatement.setDate(3,newProject.getEndDate());
                preparedStatement.setInt(4,newProject.getTeamId());
                preparedStatement.setInt(5,newProject.getId());
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

    public static void deleteObject(Project project) throws SQLException, IllegalArgumentException{
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT projects.* FROM projects WHERE id='" + project.getId() + "'"
            );
            if(selectStatement.next()){
                selectStatement.deleteRow();
            } else {
                throw new IllegalArgumentException("Project no longer in database.");
            }
        } catch (SQLException | IllegalArgumentException ex){
            throw ex;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { selectStatement.getStatement().close(); } catch (Exception ex) { };
            try { selectStatement.close(); }  catch (Exception ex) { };
        }
    }

    public static int countObjects() throws SQLException {
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        try {
            selectStatement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE).executeQuery(
                    "SELECT COUNT(*) FROM projects"
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

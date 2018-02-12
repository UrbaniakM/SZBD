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
    public static List<Project> importObject(){
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
        } catch (SQLException ex){
            System.out.println("Statement execution failed! Check output console");
            ex.printStackTrace();
            return null;
        } finally {
            try { connection.close(); }  catch (Exception ex) { };
            try { rs.close(); }  catch (Exception ex) { };
        }
    }

    public static void addObject(Project project){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sqlStatement = "INSERT INTO projects(nazwa, data_rozpoczecia, data_zakonczenia, nazwa_zespolu) VALUES " +
                    "(?,?,?)";
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1,project.getName());
            preparedStatement.setDate(2,project.getBeginDate());
            preparedStatement.setDate(3,project.getEndDate());
            preparedStatement.setString(4,project.getTeamName());
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

    public static void editObject(Project previousProject, Project newProject){ // TODO: EMPTY VALUES
        Connection connection = ApplicationGUI.databaseConnection.getConnection();
        ResultSet selectStatement = null;
        PreparedStatement preparedStatement = null;
        try {
            selectStatement = connection.createStatement().executeQuery(
                    "SELECT nazwa FROM projects WHERE nazwa='" + previousProject.getName() + "'"
            );
            if(selectStatement.next()){
                String updateStatement = "UPDATE projects SET nazwa = ?, data_rozpoczecia = ?, data_zakonczenia = ?, nazwa_zespolu = ?" +
                        "WHERE nazwa = ?";
                preparedStatement = connection.prepareStatement(updateStatement);
                preparedStatement.setString(1,newProject.getName());
                preparedStatement.setDate(2,newProject.getBeginDate());
                preparedStatement.setDate(3,newProject.getEndDate());
                preparedStatement.setString(4,newProject.getTeamName());
                preparedStatement.setString(5,previousProject.getName());
                preparedStatement.executeUpdate();
            } else {
                System.err.println("Project no longer in database. Data loss possible");
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
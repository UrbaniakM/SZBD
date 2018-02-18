package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String username;
    private static String password;

    public DatabaseConnection(String username, String password){
        this.username = username;
        this.password = password;
    }

    public static Connection getConnection(){
        Connection connection;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:9999/dblab02_students.cs.put.poznan.pl", username, password);
            /*connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl", username, password);*/

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;

        } catch (ClassNotFoundException e) {

            System.out.println("Oracle JDBC Driver failure! Check output console");
            e.printStackTrace();
            return null;
//TODO: alerts
        }
        System.out.println("Connected to database");
        return connection;
    }
}

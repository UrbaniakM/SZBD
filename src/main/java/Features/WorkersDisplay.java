package Features;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkersDisplay {

    public WorkersDisplay(Connection connection){
        try {
            Statement stmt=connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM zespoly");
            while(rs.next())
                //System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
                System.out.println(rs.getString(2)+"  "+rs.getString(3));

        } catch (SQLException ex){
            System.out.println("Statement creation failed! Check output console");
            ex.printStackTrace();
            return;
        }
    }
}

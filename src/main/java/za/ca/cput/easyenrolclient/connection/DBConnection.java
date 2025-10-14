
package za.ca.cput.easyenrolclient.connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
  public static Connection derbyConnection() throws SQLException {
        String DATABASE_URL = "jdbc:derby://localhost:1527/StudentEnrollmentDB";
        String username = "administrator";
        String password = "admin";
        
        Connection connection =  DriverManager.getConnection(DATABASE_URL, username,password);
    
return connection;
    
}

   public static Connection getConnection() {
       throw new UnsupportedOperationException("Not supported yet.");
   }
      
}
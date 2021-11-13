package BotBinance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

public static Connection CreateConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
	String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	try {
	    Class.forName(driver);
	} 
	
	catch (ClassNotFoundException e) {
	  
	    e.printStackTrace();
	} 
	String url="jdbc:sqlserver://bd2021.database.windows.net;databaseName=eduardmatei;";

	String user="eduardmateiuser";
	String password="Y-$aWWNvA7JfXRQY";
	Connection conn = DriverManager.getConnection(url, 
           user,password);


	
			

return conn;	
}
}

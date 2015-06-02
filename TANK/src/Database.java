import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class Database {
	
	private String url = "jdbc:mysql://sql5.db4free.net:3306/tankgamedata";
	private String username = "game12345";
	private String password = "123qweasd";
	
	private Connection connection = null;
	
	public Database(){
		
	}
	
	public void connect(){
		try {
		   //System.out.println("Loading driver...");
		   Class.forName("com.mysql.jdbc.Driver");
		   //System.out.println("Driver loaded!");
		    
		} catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		
		try {
		   //System.out.println("Connecting database...");
		   connection = DriverManager.getConnection(url, username, password);
		   //System.out.println("Database connected!");
		   
		} catch (SQLException e) {
		    throw new RuntimeException("Cannot connect the database!", e);
		}
	}
	
	public void write(String text, int number){
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO score_table (nickname, score) VALUES ('"+ text +"', "+ number +")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		//System.out.println("Closing the connection.");
	    if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	}
}

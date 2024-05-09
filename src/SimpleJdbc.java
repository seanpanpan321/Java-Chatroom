import java.sql.*;

public class SimpleJdbc {
  public static void main(String[] args){
    // Load the JDBC driver
    //Class.forName("com.mysql.jdbc.Driver");
    //System.out.println("Driver loaded");

    // Connect to a database
    Connection connection =null;
	try {
		connection = DriverManager.getConnection
		  ("jdbc:sqlite:javabook.db");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.exit(0);;
	}
    System.out.println("Database connected");

    // Create a statement
    Statement statement = null;
	try {
		statement = connection.createStatement();
	} catch (SQLException e) {
		e.printStackTrace();
		System.exit(0);
	}

    // Execute a statement
    ResultSet resultSet = null;
	try {
		resultSet = statement.executeQuery
		  ("select * from Student where lastName "
		    + " = 'Smith'");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.exit(0);
	}

    // Iterate through the result and print the student names
    try {
		while (resultSet.next())
		  System.out.println(resultSet.getString(1) + "\t" +
		    resultSet.getString(2) + "\t" + resultSet.getString(3));
	} catch (SQLException e) {
		e.printStackTrace();
		System.exit(0);
	}

    // Close the connection
    try {
		connection.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.exit(0);
	}
  }
}

package de.javanachhilfe.halfbroke.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class ConnectionManager {

	private static ConnectionManager connectionManager = null;

	private Connection connection = null;

	//CREATE USER 'fred'@'localhost' IDENTIFIED BY 'test';
	//GRANT ALL PRIVILEGES ON halfbroke.* TO 'fred'@'localhost';
	//DROP USER 'fred'@'localhost';
	//private static final String SQL_CREATE_TABLE = "CREATE TABLE `halfbroke`.`test` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `testcolumn` VARCHAR, PRIMARY KEY (`id`))";

	/**
	 * 
	 */
	private ConnectionManager() {
	}

	/**
	 * 
	 * @return
	 */
	public static ConnectionManager getInstance() throws Exception {
		if(connectionManager == null) {
			connectionManager = new ConnectionManager();
		}
		return connectionManager;
	}

	/**
	 * 
	 * @return
	 */
	public void connect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/halfbroke";
		String username = "fred";
		String password = "test";

		connection = DriverManager.getConnection(url, username, password);
	}
	
	/**
	 * 
	 */
	public void disconnect() {
		try {
			connection.rollback();
			connection.close();
		} catch(Exception e) {
			connection = null;
		}
	}

	/**
	 * 
	 * @param connection
	 */
//	public void creatTable(String sql) {
//		try {
//			PreparedStatement stmt = connection.prepareStatement(SQL_CREATE_TABLE);
//			// stmt.setInt(1, 101);// 1 specifies the first parameter in the query
//			// stmt.setString(2, "Ratan");
//
//			int i = stmt.executeUpdate();
//			System.out.println(i + " records inserted");
//
//			connection.close();
//
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}

}
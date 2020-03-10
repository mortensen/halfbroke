package de.javanachhilfe.halfbroke.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton instance to connect to a database
 * @author frederik.mortensen
 *
 */
public class ConnectionManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static ConnectionManager connectionManager = null;

	/**
	 * This class is a singleton, so there is no public constructor. 
	 */
	private ConnectionManager() {
	}

	/**
	 * Retrieve an insstance of this class to manage database connections using the singleton pattern.
	 * @return
	 */
	public static ConnectionManager getInstance() throws Exception {
		if (connectionManager == null) {
			connectionManager = new ConnectionManager();
		}
		return connectionManager;
	}

	/**
	 * Get a connection to the given database using the configured credentials.<br />
	 * Hint: These values should be put inside a config file.
	 * @return the jdbc database connection
	 * @throws Exception
	 */
	public Connection connect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/halfbroke";
		String username = "duke";
		String password = "test";

		Connection connection = DriverManager.getConnection(url, username, password);

		return connection;
	}

	/**
	 * Close the database connection.
	 * @param connection
	 */
	public void disconnect(Connection connection) {
		try {
			connection.rollback();
			connection.close();
		} catch (Exception e) {
			logger.info("Closing connection failed.");
		}
	}

}
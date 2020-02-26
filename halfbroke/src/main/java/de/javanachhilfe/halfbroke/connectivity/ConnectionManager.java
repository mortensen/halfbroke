package de.javanachhilfe.halfbroke.connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class ConnectionManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static ConnectionManager connectionManager = null;

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
		if (connectionManager == null) {
			connectionManager = new ConnectionManager();
		}
		return connectionManager;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection connect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String url = "jdbc:mysql://localhost:3306/halfbroke";
		String username = "fred";
		String password = "test";

		Connection connection = DriverManager.getConnection(url, username, password);

		return connection;
	}

	/**
	 * 
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
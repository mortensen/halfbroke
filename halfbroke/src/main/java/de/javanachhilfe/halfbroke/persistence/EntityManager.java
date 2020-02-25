package de.javanachhilfe.halfbroke.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javanachhilfe.halfbroke.connectivity.ConnectionManager;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class EntityManager<T> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static EntityManager entityManager = null;

	private ConnectionManager connectionManager = null;

	/**
	 * 
	 */
	private EntityManager() {

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static EntityManager getInstance() throws Exception {
		if(entityManager == null) {
			entityManager = new EntityManager();
			entityManager.setConnectionManager(ConnectionManager.getInstance());
		}
		return entityManager;
	}

	/**
	 * 
	 */
	public void test() throws Exception {

		logger.info("Connecting...");

		connectionManager.connect();
		
		Thread.sleep(1000);
		
		connectionManager.disconnect();
	}

	public T read(T entity) {
		// PreparedStatement preparedStatement;
		// PreparedStatement.
		return null;
	}

	public boolean write(T entity) {
		return false;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

}

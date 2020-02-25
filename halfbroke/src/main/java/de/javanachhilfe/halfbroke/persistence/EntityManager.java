package de.javanachhilfe.halfbroke.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
		if (entityManager == null) {
			entityManager = new EntityManager();
			entityManager.setConnectionManager(ConnectionManager.getInstance());
		}
		return entityManager;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T read(T entity) throws Exception {
		logger.info("Connecting...");
		try (Connection connection = connectionManager.connect()) {
			PreparedStatement preparedStatement = connection.prepareStatement("select * from " + entity.getClass().getSimpleName());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Long id = resultSet.getLong(1);
				logger.info("ID: " + id);
			}
			return null;
		}
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
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

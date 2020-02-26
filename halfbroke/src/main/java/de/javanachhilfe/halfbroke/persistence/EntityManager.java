package de.javanachhilfe.halfbroke.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

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
		Map<String, Object> primaryKey = null;

		primaryKey = getPrimaryKey(entity);

		String sql = buildQuery(entity, primaryKey);

		logger.info("Connecting...");
		try (Connection connection = connectionManager.connect()) {
			logger.info("Executing: " + sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.first()) {
				do {
					Long id = resultSet.getLong(1);
					logger.info("ID: " + id);
				} while (resultSet.next());
			} else {
				logger.info("Kein Objekt mit dieser ID gefunden.");
				throw new ObjectNotFoundException();
			}

			return null;

		}
	}

	/**
	 * Check if there is a field with the annotation {@link PrimaryKey} and return
	 * it's value.
	 * 
	 * @param entity
	 * @return
	 * @throws ObjectNotFoundException
	 */
	private Map<String, Object> getPrimaryKey(T entity) throws ObjectNotFoundException {
		try {
			Class<?> clazz = entity.getClass();
			for (Field field : clazz.getFields()) {
				if (field.isAnnotationPresent(PrimaryKey.class)) {
					Map<String, Object> primaryKey = new HashMap<>();
					primaryKey.put(field.getName(), field.get(entity));
					return primaryKey;
				}
			}
		} catch (IllegalAccessException e) {
			logger.error("Reflection call failed! ", e);
		}
		//in case no field has the annoation OR the reflection call failed
		throw new ObjectNotFoundException();
	}

	/**
	 * 
	 * @param entity
	 * @param primaryKey
	 * @return
	 */
	private String buildQuery(T entity, Map<String, Object> primaryKey) {
		StringBuffer stringBuffer = new StringBuffer("select * from ");
		stringBuffer.append(entity.getClass().getSimpleName());
		stringBuffer.append(" where ");
		String key = primaryKey.keySet().stream().findFirst().get();
		stringBuffer.append(key);
		stringBuffer.append(" = ");
		stringBuffer.append(primaryKey.get(key));
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param connectionManager
	 */
	private void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

}

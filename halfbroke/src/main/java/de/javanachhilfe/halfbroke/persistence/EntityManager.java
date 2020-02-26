package de.javanachhilfe.halfbroke.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javanachhilfe.halfbroke.connectivity.ConnectionManager;
import de.javanachhilfe.halfbroke.utils.StringUtils;

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
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T read(T entity) throws Exception {
		Map<String, Object> primaryKey = null;

		primaryKey = getPrimaryKey(entity);

		List<String> columns = getColumns(entity);

		String sql = buildQuery(entity, primaryKey, columns);

		T resultObject = (T) entity.getClass().getDeclaredConstructor().newInstance();

		logger.info("Connecting...");
		try (Connection connection = connectionManager.connect()) {
			logger.info("Executing: " + sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet.first()) {

				Map<String, Object> resultMapWithColumnsAndValues = new HashMap<>();

				for(String column : columns) {
					Object value = resultSet.getObject(column);
					resultMapWithColumnsAndValues.put(column, value);
				}

				mapResultToObject(resultObject, resultMapWithColumnsAndValues);

			} else {
				logger.info("Kein Objekt mit dieser ID gefunden.");
				throw new ObjectNotFoundException();
			}

			if(resultSet.next()) {
				// Objekt nicht eindeutig!
				throw new ObjectNotFoundException();
			}

			return resultObject;
		}
	}

	/**
	 * Check if there is a field with the annotation {@link PrimaryKey} and return it's value.
	 * 
	 * @param entity
	 * @return
	 * @throws ObjectNotFoundException
	 */
	private Map<String, Object> getPrimaryKey(T entity) throws ObjectNotFoundException {
		try {
			Class<?> clazz = entity.getClass();
			// getFields() wäre leer
			for(Field field : clazz.getDeclaredFields()) {
				if(field.isAnnotationPresent(PrimaryKey.class)) {
					Map<String, Object> primaryKey = new HashMap<>();
					// IllegalAccess ohne Grant
					field.setAccessible(true);
					primaryKey.put(field.getName(), field.get(entity));
					return primaryKey;
				}
			}
		} catch(IllegalAccessException e) {
			logger.error("Reflection call failed! ", e);
		}
		// in case no field has the annoation OR the reflection call failed
		throw new ObjectNotFoundException();
	}

	/**
	 * 
	 * @param entity
	 * @param primaryKey
	 * @return
	 */
	private String buildQuery(T entity, Map<String, Object> primaryKey, List<String> columns) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT ");
		String cols = columns.size() > 0 ? StringUtils.concat(',', columns) : "*";
		stringBuffer.append(cols);
		stringBuffer.append(" FROM ");
		String table = entity.getClass().getSimpleName();
		stringBuffer.append(table);
		stringBuffer.append(" WHERE ");
		String key = primaryKey.keySet().stream().findFirst().get();
		stringBuffer.append(key);
		stringBuffer.append(" = ");
		stringBuffer.append(primaryKey.get(key));
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	private List<String> getColumns(T entity) {
		List<String> columns = new ArrayList<>();
		Class<?> clazz = entity.getClass();
		for(Field field : clazz.getDeclaredFields()) {
			if(!isTransient(field)) {
				// field.setAccessible(true);
				columns.add(field.getName());
			}
		}
		return columns;
	}

	/**
	 * 
	 * @param entity
	 * @param values
	 */
	private void mapResultToObject(T entity, Map<String, Object> values) {
		Class<?> clazz = entity.getClass();
		try {
			for(String column : values.keySet()) {
				Field field = clazz.getDeclaredField(column);
				field.setAccessible(true);
				field.set(entity, values.get(column));
			}
		} catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			logger.error("Failed to map values!", e);
		}
	}

	/**
	 * keep your modifiers simple with this class
	 * 
	 * @param field
	 * @return
	 */
	private boolean isTransient(Field field) {
		return Modifier.isTransient(field.getModifiers());
	}

	/**
	 * 
	 * @param connectionManager
	 */
	private void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

}

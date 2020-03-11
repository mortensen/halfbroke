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
 * The entity manager is used for CRUD operations on the database.
 * 
 * @author frederik.mortensen
 *
 */
public class EntityManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static EntityManager entityManager = null;

	private ConnectionManager connectionManager = null;

	/**
	 * As this is a singleton instance the constructor is private.
	 */
	private EntityManager() {

	}

	/**
	 * Get or create an instance of EntityManager to be used as a singleton reference.
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
	 * @param entity the object that holds the primary which will be used to select from database.<br />
	 *            The entity type is also the database table as we don't use any table annotations.
	 * @return
	 * @throws Exception the exception handling needs work if planning to use this in production!
	 */
	public <T> T read(Class<T> clazz, T entity) throws Exception {

		Map<String, Object> primaryKeyNameAndValue = null;

		primaryKeyNameAndValue = getPrimaryKey(entity);

		List<String> columns = getColumns(entity);

		String sql = buildQuery(entity, primaryKeyNameAndValue, columns);

		T resultObject = clazz.getDeclaredConstructor().newInstance();

		logger.info("Connecting...");
		try (Connection connection = connectionManager.connect()) {

			logger.info("Executing: " + sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			// get first result
			if(resultSet.first()) {

				// read result
				Map<String, Object> resultMapWithColumnsAndValues = new HashMap<>();

				for(String column : columns) {
					// Note: we only need objects here, no parsing neccessary as we use reflections!
					Object value = resultSet.getObject(column);
					resultMapWithColumnsAndValues.put(column, value);
				}

				// fill returning entity object with the database result values
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
	 * @return a map containing the name of the primary key column and it's value
	 * @throws ObjectNotFoundException
	 */
	private <T> Map<String, Object> getPrimaryKey(T entity) throws ObjectNotFoundException {
		try {
			Class<?> clazz = entity.getClass();

			// Note: getFields() would be empty!
			for(Field field : clazz.getDeclaredFields()) {
				if(field.isAnnotationPresent(PrimaryKey.class)) {
					Map<String, Object> primaryKeyNameAndValue = new HashMap<>();
					// Note: IllegalAccessException if we don't grant access.
					field.setAccessible(true);
					primaryKeyNameAndValue.put(field.getName(), field.get(entity));
					return primaryKeyNameAndValue;
				}
			}
		} catch(IllegalAccessException e) {
			logger.error("Reflection call failed! ", e);
		}
		// in case no field has the annoation OR the reflection call failed
		logger.error("Didn't find any primary key!");
		throw new ObjectNotFoundException();
	}

	/**
	 * Now that we have the table object, the primary key field and value and the columns we can produce a select.
	 * 
	 * @param entity the table we want to select from
	 * @param primaryKey the key and value for the database primary key
	 * @param columns the database columns to select
	 * @return the generated sql select call.
	 */
	private <T> String buildQuery(T entity, Map<String, Object> primaryKey, List<String> columns) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("SELECT ");
		// if there are no columns given, use * as fallback
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
	 * As we don't use a column annotation we get every field that is not defined as transient.
	 * 
	 * @param entity
	 * @return
	 */
	private <T> List<String> getColumns(T entity) {
		List<String> columns = new ArrayList<>();
		Class<?> clazz = entity.getClass();
		for(Field field : clazz.getDeclaredFields()) {
			if(isTransient(field)) {
				continue;
			}
			if(field.getName().equals("serialVersionUID")) {
				continue;
			}
			columns.add(field.getName());

		}
		return columns;
	}

	/**
	 * fill result object with the database result values
	 * 
	 * @param entity
	 * @param values
	 */
	private <T> void mapResultToObject(T entity, Map<String, Object> values) {
		Class<?> clazz = entity.getClass();
		try {
			for(String column : values.keySet()) {
				// Note: we know the columns should exist as the got them through reflection in the first place!
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

package de.javanachhilfe.halfbroke.persistence;

import java.sql.ConnectionBuilder;
import java.sql.PreparedStatement;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class EntityManager<T> {
	
	public T read(T entity) {
		Connection connection = ConnectionManager.getConnection();
		PreparedStatement preparedStatement;
		PreparedStatement.
		return null;
	}
	
	public boolean write(T entity) {
		
		return false;
	}

}

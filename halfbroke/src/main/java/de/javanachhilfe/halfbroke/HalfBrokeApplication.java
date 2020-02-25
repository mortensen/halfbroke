package de.javanachhilfe.halfbroke;

import de.javanachhilfe.halfbroke.model.Person;
import de.javanachhilfe.halfbroke.persistence.EntityManager;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class HalfBrokeApplication {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new HalfBrokeApplication().start();

	}
	
	/**
	 * 
	 */
	private void start() throws Exception {
		EntityManager entityManager = EntityManager.getInstance();
		Person person = new Person();
		entityManager.read(person);
	}

}

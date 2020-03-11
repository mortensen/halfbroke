package de.javanachhilfe.halfbroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.javanachhilfe.halfbroke.model.Person;
import de.javanachhilfe.halfbroke.persistence.EntityManager;

/**
 * Main class to start the demo
 * @author frederik.mortensen
 *
 */
public class HalfBrokeApplication {

	private Logger logger = LoggerFactory.getLogger(getClass());

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
		person.setId(1l);
		Person loadedPerson = entityManager.read(Person.class, person);
		logger.info("Found person with name: " + loadedPerson.getFirstName() + " " + loadedPerson.getLastName());
	}

}

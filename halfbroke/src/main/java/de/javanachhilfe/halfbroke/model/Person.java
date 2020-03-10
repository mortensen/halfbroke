package de.javanachhilfe.halfbroke.model;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class Person extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	/**
	 * not being persisted to database
	 */
	private transient String comment;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}

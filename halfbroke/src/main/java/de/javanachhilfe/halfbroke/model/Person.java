package de.javanachhilfe.halfbroke.model;

import de.javanachhilfe.halfbroke.persistence.PrimaryKey;

/**
 * 
 * @author frederik.mortensen
 *
 */
public class Person {

	@PrimaryKey
	private Long id;

	private String firstName;

	private String lastName;

	private transient String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

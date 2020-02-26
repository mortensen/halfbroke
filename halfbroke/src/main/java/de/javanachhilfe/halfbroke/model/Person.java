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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

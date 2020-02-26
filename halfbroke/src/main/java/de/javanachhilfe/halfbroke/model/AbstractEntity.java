package de.javanachhilfe.halfbroke.model;

import java.io.Serializable;

import de.javanachhilfe.halfbroke.persistence.PrimaryKey;

/**
 * 
 * @author frederik.mortensen
 *
 */
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

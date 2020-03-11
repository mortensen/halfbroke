package de.javanachhilfe.halfbroke.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker interface for the field that is used as primary key on database selects
 * @author frederik.mortensen
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
public @interface PrimaryKey {

}
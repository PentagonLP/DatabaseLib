package de.pentagonlp.database.exceptions;

import de.pentagonlp.database.DatabaseConnection;

/**
 * {@link Exception} which is thrown if {@link DatabaseConnection#open()} or
 * {@link DatabaseConnection#close()} is called but the
 * {@link DatabaseConnection} is already open/closed
 * 
 * @author PentagonLP
 * 
 */
public class NoConectionStatusChangeException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a {@link NoConectionStatusChangeException}
	 * 
	 * @param text The Message of the {@link Exception}
	 * 
	 */
	public NoConectionStatusChangeException(String text) {
		super(text);
	}

}

package de.pentagonlp.database.exceptions;

/**
 * {@link Exception} which is thrown if connecting to the database fails due to
 * invalid connection details
 * 
 * @author PentagonLP
 * 
 */
public class InvalidConnectionDetailsException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a {@link InvalidConnectionDetailsException}
	 * 
	 * @param text The Message of the {@link Exception}
	 * 
	 */
	public InvalidConnectionDetailsException(String text) {
		super(text);
	}

}

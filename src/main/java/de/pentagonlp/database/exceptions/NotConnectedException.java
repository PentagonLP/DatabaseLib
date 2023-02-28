package de.pentagonlp.database.exceptions;

/**
 * {@link Exception} which is thrown if a SQL query is executed but the
 * connection to the database is closed
 * 
 * @author PentagonLP
 * 
 */
public class NotConnectedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a {@link NotConnectedException}
	 * 
	 * @param text The Message of the {@link Exception}
	 * 
	 */
	public NotConnectedException(String text) {
		super(text);
	}

}

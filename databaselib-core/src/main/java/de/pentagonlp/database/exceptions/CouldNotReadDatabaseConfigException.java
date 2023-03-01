package de.pentagonlp.database.exceptions;

/**
 * {@link Exception} which is thrown if a parsing error occures when parsing a
 * file with connection details
 * 
 * @author PentagonLP
 * 
 */
public class CouldNotReadDatabaseConfigException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a {@link CouldNotReadDatabaseConfigException}
	 * 
	 * @param text The Message of the {@link Exception}
	 * 
	 */
	public CouldNotReadDatabaseConfigException(String text) {
		super(text);
	}

}

package de.pentagonlp.database.exceptions;

public class CouldNotReadDatabaseConfigException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CouldNotReadDatabaseConfigException(String text) {
		super(text);
	}

}

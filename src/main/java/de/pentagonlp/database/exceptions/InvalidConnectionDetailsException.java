package de.pentagonlp.database.exceptions;

public class InvalidConnectionDetailsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidConnectionDetailsException(String text) {
		super(text);
	}

}

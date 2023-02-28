package de.pentagonlp.database.exceptions;

public class NotConnectedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NotConnectedException(String text) {
		super(text);
	}

}

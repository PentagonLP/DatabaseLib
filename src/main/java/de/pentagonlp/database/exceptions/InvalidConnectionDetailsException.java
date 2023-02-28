package de.pentagonlp.database.exceptions;

public class InvalidConnectionDetailsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final String host;
	private final String port;
	private final String database;
	private final String username;
	private final String password;
	
	public InvalidConnectionDetailsException(String Host, String Port, String Database, String Username, String Password) {
		host = Host;
    	port = Port;
    	database = Database;
    	username = Username;
    	password = Password;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}

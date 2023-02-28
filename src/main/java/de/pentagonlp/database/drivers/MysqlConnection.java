package de.pentagonlp.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.pentagonlp.database.drivertypes.DatabaseServerConnection;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

public class MysqlConnection extends DatabaseServerConnection {
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 3306;
	public static final String DEFAULT_DATABASE = "";
	public static final String DEFAULT_USERNAME = "root";
	public static final String DEFAULT_PASSWORD = "";

	public MysqlConnection() throws InvalidConnectionDetailsException {
		super();
	}

	public MysqlConnection(String Host, int Port, String Database, String Username, String Password,
			boolean autoRecon) throws InvalidConnectionDetailsException {
		super(Host, Port, Database, Username, Password, autoRecon);
	}

	public MysqlConnection(String Host, int Port, String Database, String Username, String Password)
			throws InvalidConnectionDetailsException {
		super(Host, Port, Database, Username, Password);
	}

	@Override
	public Connection getConnection(String host, int port, String database, String username, String password) throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useTimezone=true&serverTimezone=UTC",username, password);
	}
	
	/**
     * Set Default Values for Database connection
     * @throws InvalidConnectionDetailsException 
     * 
     */
    public void setDefaultConnectionValues() throws InvalidConnectionDetailsException {
    	setConnectionValues(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

	

}

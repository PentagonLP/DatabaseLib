package de.pentagonlp.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.pentagonlp.database.drivertypes.DatabaseServerConnection;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

/**
 * Connection to a MySql Server
 * 
 * @author PentagonLP
 */
public class MysqlConnection extends DatabaseServerConnection {

	/**
	 * Default MySql port
	 */
	public static final int DEFAULT_PORT = 3306;
	/**
	 * Select no database if none is given
	 */
	public static final String DEFAULT_DATABASE = "";
	/**
	 * Default MySql username
	 */
	public static final String DEFAULT_USERNAME = "root";
	/**
	 * Default password for default MySql user {@code root} is no password
	 */
	public static final String DEFAULT_PASSWORD = "";

	/**
	 * Creates a {@link MysqlConnection} with default connection details and
	 * {@code autoReconnect = false}
	 * 
	 */
	public MysqlConnection() throws InvalidConnectionDetailsException {
		super();
	}

	/**
	 * Creates a {@link MysqlConnection} with given connection details and
	 * {@code autoReconnect = false}
	 * 
	 * @param host     Hostname of the MySql Server
	 * @param port     Port of the MySql Server
	 * @param database Database to use
	 * @param username Username to log into the MySql Server with
	 * @param password Password to log into the MySql Server with
	 * 
	 */
	public MysqlConnection(String host, int port, String database, String username, String password)
			throws InvalidConnectionDetailsException {
		super(host, port, database, username, password);
	}

	/**
	 * Creates a {@link MysqlConnection} with given connection details and given
	 * value for {@code autoReconnect}
	 * 
	 * @param host          Hostname of the MySql Server
	 * @param port          Port of the MySql Server
	 * @param database      Database to use
	 * @param username      Username to log into the MySql Server with
	 * @param password      Password to log into the MySql Server with
	 * @param autoReconnect Enables/disables auto reconnect for this connection
	 * 
	 */
	public MysqlConnection(String host, int port, String database, String username, String password,
			boolean autoReconnect) throws InvalidConnectionDetailsException {
		super(host, port, database, username, password, autoReconnect);
	}

	/**
	 * Create a new {@link Connection} to a MySql Server
	 * 
	 */
	@Override
	protected Connection createConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase()
				+ "?autoReconnect=true&useTimezone=true&serverTimezone=UTC", getUsername(), getPassword());
	}

	/**
	 * Set Default Values for Database connection
	 * 
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public void setDefaultConnectionDetails() throws InvalidConnectionDetailsException {
		setConnectionDetails(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}

}

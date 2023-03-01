package de.pentagonlp.database.drivertypes;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import de.pentagonlp.database.DataElement;
import de.pentagonlp.database.DatabaseConnection;
import de.pentagonlp.database.exceptions.CouldNotReadDatabaseConfigException;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

/**
 * Superclass for all database connections connecting to an IP server
 * 
 * @author PentagonLP
 * 
 */
public abstract class DatabaseServerConnection extends DatabaseConnection {

	/**
	 * Default hostname for connections
	 * 
	 */
	public static final String DEFAULT_HOST = "localhost";

	/**
	 * Host of the database server
	 * 
	 */
	private String host;
	/**
	 * Port of the database server
	 * 
	 */
	private int port;
	/**
	 * Database to use on the server
	 * 
	 */
	private String database;
	/**
	 * Username to use to log into the server
	 * 
	 */
	private String username;
	/**
	 * Password to use to log into the server
	 * 
	 */
	private String password;

	/**
	 * Creates a new {@link DatabaseServerConnection} with default connection
	 * details and {@code autoReconnect = false}
	 * 
	 */
	public DatabaseServerConnection() throws InvalidConnectionDetailsException {
		super();
		setDefaultConnectionDetails();
	}

	/**
	 * Creates a {@link DatabaseServerConnection} with given connection details and
	 * {@code autoReconnect = false}
	 * 
	 * @param host     Hostname of the database server
	 * @param port     Port of the database server
	 * @param database Database to use on the server
	 * @param username Username to log into the database server with
	 * @param password Password to log into the database server with
	 * 
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public DatabaseServerConnection(String host, int port, String database, String username, String password)
			throws InvalidConnectionDetailsException {
		setConnectionDetails(host, port, database, username, password);
	}

	/**
	 * Creates a {@link DatabaseServerConnection} with given connection details and
	 * given value for {@code autoReconnect}
	 * 
	 * @param host          Hostname of the database server
	 * @param port          Port of the database server
	 * @param database      Database to use on the server
	 * @param username      Username to log into the database server with
	 * @param password      Password to log into the database server with
	 * @param autoReconnect Enables/disables auto reconnect for this connection
	 * 
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public DatabaseServerConnection(String host, int port, String database, String username, String password,
			boolean autoReconnect) throws InvalidConnectionDetailsException {
		super(autoReconnect);
		setConnectionDetails(host, port, database, username, password);
	}

	/**
	 * Set Connection Parameters
	 * 
	 * @param host     Hostname of the database server
	 * @param port     Port of the database server
	 * @param database Database to use on the server
	 * @param username Username to log into the database server with
	 * @param password Password to log into the database server with
	 * 
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public void setConnectionDetails(String host, int port, String database, String username, String password)
			throws InvalidConnectionDetailsException {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		try {
			autoReconnect();
		} catch (SQLException e) {
			throw new InvalidConnectionDetailsException("Unable to auto reconnect with the set connection values");
		}
	}

	/**
	 * Set default connection details. Implemented by the driver as default
	 * connection details for different database servers are different
	 * 
	 */
	public abstract void setDefaultConnectionDetails() throws InvalidConnectionDetailsException;

	/**
	 * Loads the connection details from a file, given through a path<br>
	 * <br>
	 * Accepted parameters:<br>
	 * {@code host=HOSTNAME}<br>
	 * {@code port=SERVERPORT}<br>
	 * {@code database=DATABASE}<br>
	 * {@code username=USERNAME}<br>
	 * {@code password=PASSWORD}<br>
	 * 
	 * @param path Path to the configuration file
	 * 
	 * @throws CouldNotReadDatabaseConfigException
	 * @throws IOException
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	@Override
	public void loadConnectionDetailsFromFile(String path)
			throws CouldNotReadDatabaseConfigException, IOException, InvalidConnectionDetailsException {
		loadConnectionDetailsFromFile(new File(path));
	}

	/**
	 * Loads the connection details from a file, given through a {@link File}<br>
	 * <br>
	 * Accepted parameters:<br>
	 * {@code host=HOSTNAME}<br>
	 * {@code port=SERVERPORT}<br>
	 * {@code database=DATABASE}<br>
	 * {@code username=USERNAME}<br>
	 * {@code password=PASSWORD}<br>
	 * 
	 * @param file The configuation file
	 * 
	 * @throws CouldNotReadDatabaseConfigException
	 * @throws IOException
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	@Override
	public void loadConnectionDetailsFromFile(File file)
			throws CouldNotReadDatabaseConfigException, IOException, InvalidConnectionDetailsException {

		HashMap<String, DataElement> fileentries = readConfigurationFile(file);

		// TODO make this a bit cleaner? Kind of copy-pastes almost the same code 5
		// times
		if (fileentries.containsKey("host"))
			host = fileentries.get("host").toString();
		else
			throw new CouldNotReadDatabaseConfigException("Missing entry 'host' from configuratioon file");

		if (fileentries.containsKey("port"))
			port = fileentries.get("port").toInt();
		else
			throw new CouldNotReadDatabaseConfigException("Missing entry 'port' from configuratioon file");

		if (fileentries.containsKey("database"))
			database = fileentries.get("database").toString();
		else
			throw new CouldNotReadDatabaseConfigException("Missing entry 'database' from configuratioon file");

		if (fileentries.containsKey("username"))
			username = fileentries.get("username").toString();
		else
			throw new CouldNotReadDatabaseConfigException("Missing entry 'username' from configuratioon file");

		if (fileentries.containsKey("password"))
			password = fileentries.get("password").toString();
		else
			throw new CouldNotReadDatabaseConfigException("Missing entry 'password' from configuratioon file");

		connectionDetailsUpdate();

	}

	/**
	 * Gets the hostname of the database server
	 * 
	 * @return The hostname of the database server
	 * 
	 * @see DatabaseServerConnection#setConnectionDetails(String, int, String,
	 *      String, String)
	 * 
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the port of the database server
	 * 
	 * @return The port of the database server
	 * 
	 * @see DatabaseServerConnection#setConnectionDetails(String, int, String,
	 *      String, String)
	 * 
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Gets the database which was selected at when the connection was established
	 * 
	 * @return The database which was selected at when the connection was
	 *         established
	 * 
	 * @see DatabaseServerConnection#setConnectionDetails(String, int, String,
	 *      String, String)
	 * 
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * Gets the username used to connect to the database server
	 * 
	 * @return The username used to connect to the database server
	 * 
	 * @see DatabaseServerConnection#setConnectionDetails(String, int, String,
	 *      String, String)
	 * 
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password used to connect to the database server
	 * 
	 * @return The password used to connect to the database server
	 * 
	 * @see DatabaseServerConnection#setConnectionDetails(String, int, String,
	 *      String, String)
	 * 
	 */
	protected String getPassword() {
		return password;
	}

}

package de.pentagonlp.database.drivers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import de.pentagonlp.database.DataElement;
import de.pentagonlp.database.DatabaseConnection;
import de.pentagonlp.database.exceptions.CouldNotReadDatabaseConfigException;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

/**
 * Interface to use a Sqlite file
 * 
 * @author PentagonLP
 * 
 */
public class SqlLiteConnection extends DatabaseConnection {

	/**
	 * Path to the Sqlite database file
	 * 
	 */
	private String pathToFile;

	/**
	 * Creates an Interface to open a Sqllite file
	 * 
	 * @param pathToFile    The path to the file
	 * @param autoReconnect Specifies whether the Interface will attempt to reopen
	 *                      the file in case something goes wrong
	 * 
	 */
	public SqlLiteConnection(String pathToFile, boolean autoReconnect) throws InvalidConnectionDetailsException {
		super(autoReconnect);
		this.pathToFile = pathToFile;
	}

	/**
	 * Creates an Interface to open a Sqllite file<br>
	 * {@code autoReconnect} is set to {@code false}
	 * 
	 * @param pathToFile The path to the file
	 * 
	 */
	public SqlLiteConnection(String pathToFile) throws InvalidConnectionDetailsException {
		super();
		this.pathToFile = pathToFile;
	}

	/**
	 * Creates a {@link Connection} to a Sqlite file
	 * 
	 * @return A {@link Connection} to the Sqlite file
	 * 
	 */
	@Override
	protected Connection createConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + pathToFile);
	}

	public void setConnectionDetails(String pathToFile) throws InvalidConnectionDetailsException {
		this.pathToFile = pathToFile;
		connectionDetailsUpdate();
	}

	/**
	 * Loads the connection details from a file, given through a path<br>
	 * <br>
	 * Accepted parameters:<br>
	 * {@code path=PATHTOFILE}
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
	 * {@code path=PATHTOFILE}
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

		if (fileentries.containsKey("path"))
			pathToFile = fileentries.get("path").toString();
		else
			throw new CouldNotReadDatabaseConfigException("Missing entry 'path' from configuration file");

		connectionDetailsUpdate();
	}

}

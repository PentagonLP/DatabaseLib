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

public class SqlLiteConnection extends DatabaseConnection {
	
	private String pathToFile;

	public SqlLiteConnection() throws InvalidConnectionDetailsException {
		super();
	}

	public SqlLiteConnection(String pathToFile, boolean autoRecon) throws InvalidConnectionDetailsException {
		super(autoRecon);
		this.pathToFile = pathToFile;
	}

	public SqlLiteConnection(String pathToFile) throws InvalidConnectionDetailsException {
		super();
		this.pathToFile = pathToFile;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + pathToFile);
	}

	@Override
	public void loadConfigFromFile(String path) throws CouldNotReadDatabaseConfigException, IOException {
		loadConfigFromFile(new File(path));
	}

	@Override
	public void loadConfigFromFile(File file) throws CouldNotReadDatabaseConfigException, IOException {
		HashMap<String, DataElement> fileentries = readConfigurationFile(file);
		
		if (fileentries.containsKey("path"))
    		pathToFile = fileentries.get("path").toString();
    	else
    		throw new CouldNotReadDatabaseConfigException("Missing entry 'path' from configuratioon file");
	}

}

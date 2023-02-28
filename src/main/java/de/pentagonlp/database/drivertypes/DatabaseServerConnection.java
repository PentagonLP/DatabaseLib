package de.pentagonlp.database.drivertypes;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import de.pentagonlp.database.DataElement;
import de.pentagonlp.database.DatabaseConnection;
import de.pentagonlp.database.exceptions.CouldNotReadDatabaseConfigException;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

public abstract class DatabaseServerConnection extends DatabaseConnection {
	
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	
	public DatabaseServerConnection() throws InvalidConnectionDetailsException {
		setDefaultConnectionValues();
	}
    
    public DatabaseServerConnection(String Host, int Port, String Database, String Username, String Password) throws InvalidConnectionDetailsException {
    	setConnectionValues(Host, Port, Database, Username, Password);
    }
    
	public DatabaseServerConnection(String Host, int Port, String Database, String Username, String Password, boolean autoReconnect) throws InvalidConnectionDetailsException {
		super(autoReconnect);
		setConnectionValues(Host, Port, Database, Username, Password);
	}
	
	@Override
	protected Connection getConnection() throws SQLException {
		return getConnection(host, port, database, username, password);
	}
	
	protected abstract Connection getConnection(String host, int port, String database, String username, String password) throws SQLException;
	
	public abstract void setDefaultConnectionValues() throws InvalidConnectionDetailsException;
	
	/**
     * 
     * Set Connection Parameters
     * 
     * @param Host
     * @param Port
     * @param Database
     * @param Username
     * @param Password
     * 
     * @throws InvalidConnectionDetailsException
     */
    public void setConnectionValues(String Host, int Port, String Database, String Username, String Password) throws InvalidConnectionDetailsException {
    	host = Host;
    	port = Port;
    	database = Database;
    	username = Username;
    	password = Password;
    	try {
    		autoReconnect();
		} catch (SQLException e) {
			throw new InvalidConnectionDetailsException("Unable to auto reconnect with the set connection values");
		}
    }
    
    @Override
    public void loadConfigFromFile(String path) throws CouldNotReadDatabaseConfigException, IOException {
    	loadConfigFromFile(new File(path));
    }
    
    @Override
    public void loadConfigFromFile(File file) throws CouldNotReadDatabaseConfigException, IOException {
    	
    	HashMap<String, DataElement> fileentries = readConfigurationFile(file);
    	
    	// TODO make this a bit cleaner? Kind of copy-pastes the almost same code 5 times
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
    	
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}

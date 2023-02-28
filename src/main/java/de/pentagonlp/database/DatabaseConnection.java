package de.pentagonlp.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import de.pentagonlp.database.exceptions.CouldNotReadDatabaseConfigException;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;
import de.pentagonlp.database.exceptions.NoConectionStatusChangeException;
import de.pentagonlp.database.exceptions.NotConnectedException;

/**
 * @author PentagonLP
 *
 */
public abstract class DatabaseConnection {
	
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_PORT = "3306";
	public static final String DEFAULT_DATABASE = "";
	public static final String DEFAULT_USERNAME = "root";
	public static final String DEFAULT_PASSWORD = "";
	
	private String host = "";
	private String port = "";
	private String database = "";
	private String username = "";
	private String password = "";
    private Connection con;
    
    private boolean autoReconnect = false;
	
    public DatabaseConnection() throws InvalidConnectionDetailsException {
    	setDefaultConnectionValues();
    }
    
    public DatabaseConnection(String Host, String Port, String Database, String Username, String Password) throws InvalidConnectionDetailsException {
    	setConnectionValues(Host, Port, Database, Username, Password);
    }
    
	public DatabaseConnection(String Host, String Port, String Database, String Username, String Password, boolean autoRecon) throws InvalidConnectionDetailsException {
		setConnectionValues(Host, Port, Database, Username, Password);
		autoReconnect = autoRecon;
	}
    
	/**
	 * Open Connection to Database
	 * 
	 * @throws NoConectionStatusChangeException 
	 * @throws SQLException 
	 * 
	 */
    public void open() throws NoConectionStatusChangeException, SQLException {
        if (isConnected()==false) {
        	con = getConnection(host, port, database, username, password);
        } else {
        	throw new NoConectionStatusChangeException("Already Connected!");
        }
    }
    
    public abstract Connection getConnection(String host, String port, String database, String username, String password) throws SQLException;
    
    /**
	 * Close Connection to Database
	 * 
	 * @throws NoConectionStatusChangeException 
	 * @throws SQLException 
	 * 
	 */
    public void close() throws NoConectionStatusChangeException, SQLException {
        if (isConnected()==true) {
            con.close();
            con = null;
        } else {
        	throw new NoConectionStatusChangeException("Not Connected!");
        }
    }
    
    /**
     * 
     * Load Configuration from a file, specified by a path.<br>
     * <br>
     * {@code host=YOURHOST}<br>
     * {@code port=YOURPORT}<br>
     * {@code database=DATABASE}<br>
     * {@code user=USERNAME}<br>
     * {@code password=PASSWORD}<br>
     * 
     * @param path
     * 
     * @throws CouldNotReadDatabaseConfigException
     * @throws FileNotFoundException
     */
    public void loadFromFile(String path) throws CouldNotReadDatabaseConfigException, FileNotFoundException {
    	loadFromFile(new File(path));
    }
    
    /**
     * 
     * Load Configuration from a file, specified by a file.<br>
     * <br>
     * {@code host=YOURHOST}<br>
     * {@code port=YOURPORT}<br>
     * {@code database=DATABASE}<br>
     * {@code user=USERNAME}<br>
     * {@code password=PASSWORD}<br>
     * 
     * @param file
     * 
     * @throws CouldNotReadDatabaseConfigException
     * @throws FileNotFoundException
     */
    public void loadFromFile(File file) throws CouldNotReadDatabaseConfigException, FileNotFoundException {
			Scanner scanfile = new Scanner(file);
			
			while (scanfile.hasNext()) {
				String typ = "";
				String wert = "";
				typ = scanfile.next();
				if (scanfile.hasNext()) {
					wert = scanfile.next();
					switch (typ) {
						case "host=": host = wert; break;
						case "port=": port = wert; break;
						case "database=": database = wert; break;
						case "user=": username = wert; break;
						case "password=": password = wert; break;
					}
				} else if (!typ.equals("password=")) {
					scanfile.close();
					throw new CouldNotReadDatabaseConfigException("Missing " + typ);
				} else {
					password = "";
				}
			}
			
			scanfile.close();
    }
    
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
    public void setConnectionValues(String Host, String Port, String Database, String Username, String Password) throws InvalidConnectionDetailsException {
    	host = Host;
    	port = Port;
    	database = Database;
    	username = Username;
    	password = Password;
    	try {
    		autoReconnect();
		} catch (SQLException e) {
			throw new InvalidConnectionDetailsException(Host,Port,Database,Username,Password);
		}
    }
    
    /**
     * 
     * Reconnect if auto reconnect is active
     * 
     * @return autoReconnect activ
     * 
     * @throws SQLException
     */
    private boolean autoReconnect() throws SQLException {
    	if (autoReconnect) reconnect();
    	return autoReconnect;
    }
    
    /**
     * 
     * Reconnect to Database
     * 
     * @throws SQLException
     */
    private void reconnect() throws SQLException {
    	
    	if (isConnected()) {
    		try {
    			close();
    			open();
    		} catch (NoConectionStatusChangeException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
    }
    
    /**
     * Connected to Database?
     * 
     * @return Connection status to Database
     * 
     */
    public boolean isConnected() { 
    	return !(con==null);
    }
    
    /**
     * Set Default Values for Database connection
     * @throws InvalidConnectionDetailsException 
     * 
     */
    public void setDefaultConnectionValues() throws InvalidConnectionDetailsException {
    	setConnectionValues(DEFAULT_HOST, DEFAULT_PORT, DEFAULT_DATABASE, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }
    
    /*
     * QUARRYS
     */
    
    public ArrayList<HashMap<String, DataElement>> getTable(String sql, Object...params) throws SQLException, NotConnectedException {
        
    	ArrayList<HashMap<String, DataElement>> result = new ArrayList<>();
    	
    	if (!isConnected())
    		if (autoReconnect) {
    			try {
    				open();
    			} catch (NoConectionStatusChangeException e) {
    				e.printStackTrace();
    			} 
    		} else
    			throw new NotConnectedException("Failed to run \"" + sql + "\"");
    	
    	PreparedStatement statement = con.prepareStatement(sql);
    	
    	try {
    		for (int i = 0; i<params.length; i++)
    			statement.setString(i+1, params[i].toString());
    		
    		ResultSet sqlresult = statement.executeQuery();
    		
    		try {
    			
    			// TODO dejank this; somehow fetch type of "result set", eg. "update" or "select"
    			if (!sql.toLowerCase().startsWith("select")||!sqlresult.isBeforeFirst())
    				return result;
    			
    			while (sqlresult.next()) {
    				HashMap<String,DataElement> currentrow = new HashMap<>();
    				
    				for (int collumid = 1; collumid<=sqlresult.getMetaData().getColumnCount(); collumid++)
    					currentrow.put(sqlresult.getMetaData().getColumnName(collumid), new DataElement(sqlresult.getString(collumid)));
    					
    				result.add(currentrow);
    			}
    			
    		} finally {
    			sqlresult.close();
    		}
    	} finally {
    		statement.close();
    	}
    	
    	return result;
    	
    }
    
    /**
     * Fetch single row from Database
     * @param Sqlbefehl
     * @param Spalte
     * 
     * @return Hash Map with Values from Collums
     * @throws SQLException 
     * @throws NotConnectedException 
     * 
     */
    public HashMap<String, DataElement> fastget(String sql, Object...params) throws SQLException, NotConnectedException {
    	ArrayList<HashMap<String, DataElement>> table = getTable(sql, params);
    	
    	if (table.isEmpty())
    		return null;
    	return table.get(0);
    }

	public boolean autoReconnectActive() {
		return autoReconnect;
	}
	public void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}
	
}

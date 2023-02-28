package de.pentagonlp.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import de.pentagonlp.database.exceptions.CouldNotReadDatabaseConfigException;
import de.pentagonlp.database.exceptions.NoConectionStatusChangeException;
import de.pentagonlp.database.exceptions.NotConnectedException;

/**
 * @author PentagonLP
 *
 */
public abstract class DatabaseConnection {
	
	private Connection con;
	
    private boolean autoReconnect = false;
    
    public DatabaseConnection() {}
    
    public DatabaseConnection(boolean autoReconnect) {
    	this.autoReconnect = autoReconnect;
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
        	con = getConnection();
        } else {
        	throw new NoConectionStatusChangeException("Already Connected!");
        }
    }
    
    protected abstract Connection getConnection() throws SQLException;
    
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
     * Load Configuration from a file, specified by a file.<br>
     * <br>
     * Expects list of entries in file following pattern {@code NAME=VALUE\n}<br>
     * Can handle Comments starting with {@code #}
     * 
     * @param file
     * 
     * @return HashMap with {@code NAME} as keys and {@code VALUE} as entries
     * 
     * @throws CouldNotReadDatabaseConfigException
     * @throws IOException 
     */
    protected static HashMap<String, DataElement> readConfigurationFile(File file) throws CouldNotReadDatabaseConfigException, IOException {
    	
		HashMap<String, DataElement> result = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		try {
			
			String line = reader.readLine();
			
			while (line!=null) {
				
				// Ignore Comments
				if (line.startsWith("#"))
					continue;
				
				String split[] = line.split("=");
				
				if (split.length!=2)
					throw new CouldNotReadDatabaseConfigException(String.format("Line '%s' does not follow pattern 'NAME=VALUE'", line));
				
				result.put(split[0], new DataElement(split[1]));
				
				line = reader.readLine();
			}
			
		} finally {
			reader.close();
		}
		
		return result;
		
    }
    
    public abstract void loadConfigFromFile(String path) throws CouldNotReadDatabaseConfigException, IOException;
    public abstract void loadConfigFromFile(File file) throws CouldNotReadDatabaseConfigException, IOException;
    
    /**
     * 
     * Reconnect if auto reconnect is active
     * 
     * @return autoReconnect activ
     * 
     * @throws SQLException
     */
    protected boolean autoReconnect() throws SQLException {
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
    		
    		// TODO dejank this; somehow fetch type of result, eg. "update" or "select"
    		if (!sql.toLowerCase().startsWith("select")) {
    			statement.execute();
    			return result;
    		}
    		
    		ResultSet sqlresult = statement.executeQuery();
    		
    		try {
    			
    			if (!sqlresult.isBeforeFirst())
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

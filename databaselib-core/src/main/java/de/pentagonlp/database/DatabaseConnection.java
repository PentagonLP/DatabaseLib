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
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;
import de.pentagonlp.database.exceptions.NoConectionStatusChangeException;
import de.pentagonlp.database.exceptions.NotConnectedException;

/**
 * Super class for all database connections. Handels SQL queries, auto
 * reconnects and loading connection details from a file.
 * 
 * @author PentagonLP
 *
 */
public abstract class DatabaseConnection {

	private Connection con;

	private boolean autoReconnect = false;

	/**
	 * Construct {@link DatabaseConnection} Object<br>
	 * {@code autoReconnect} is set to {@code false}
	 * 
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public DatabaseConnection() throws InvalidConnectionDetailsException {
	}

	/**
	 * Construct {@link DatabaseConnection} Object
	 * 
	 * @param autoReconnect Specifies whether the database should automatically
	 *                      reconnect if the connection is lost
	 * 
	 */
	public DatabaseConnection(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	/**
	 * Open connection to database
	 * 
	 * @throws NoConectionStatusChangeException
	 * @throws SQLException
	 * 
	 */
	public void open() throws NoConectionStatusChangeException, SQLException {
		if (isConnected() == false) {
			con = createConnection();
		} else {
			throw new NoConectionStatusChangeException("Already Connected!");
		}
	}

	protected abstract Connection createConnection() throws SQLException;

	/**
	 * Close connection to database
	 * 
	 * @throws NoConectionStatusChangeException
	 * @throws SQLException
	 * 
	 */
	public void close() throws NoConectionStatusChangeException, SQLException {
		if (isConnected() == true) {
			con.close();
			con = null;
		} else {
			throw new NoConectionStatusChangeException("Not Connected!");
		}
	}

	/**
	 * Load Configuration from a file, specified by a file.<br>
	 * <br>
	 * Expects list of entries in file following pattern {@code NAME=VALUE\n}<br>
	 * Can handle Comments starting with {@code #}
	 * 
	 * @param file
	 * 
	 * @return {@link HashMap} with {@code NAME} as keys and {@code VALUE} as
	 *         entries
	 * 
	 * @throws CouldNotReadDatabaseConfigException
	 * @throws IOException
	 * 
	 */
	protected static HashMap<String, DataElement> readConfigurationFile(File file)
			throws CouldNotReadDatabaseConfigException, IOException {

		HashMap<String, DataElement> result = new HashMap<>();
		BufferedReader reader = new BufferedReader(new FileReader(file));

		try {
			String line = reader.readLine();

			while (line != null) {
				// Ignore Comments
				if (line.startsWith("#"))
					continue;

				String split[] = line.split("=");
				if (split.length != 2)
					throw new CouldNotReadDatabaseConfigException(
							String.format("Line '%s' does not follow pattern 'NAME=VALUE'", line));

				result.put(split[0], new DataElement(split[1]));
				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
		return result;

	}

	/**
	 * Loads the connection details from a file, given through a path.<br>
	 * Accepted parameters depend on the type of the database connection.<br>
	 * 
	 * @param path Path to the configuration file
	 * 
	 * @throws CouldNotReadDatabaseConfigException
	 * @throws IOException
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public abstract void loadConnectionDetailsFromFile(String path)
			throws CouldNotReadDatabaseConfigException, IOException, InvalidConnectionDetailsException;

	/**
	 * Loads the connection details from a file, given through a {@link File}<br>
	 * Accepted parameters depend on the type of the database connection.<br>
	 * 
	 * @param file The configuration file
	 * 
	 * @throws CouldNotReadDatabaseConfigException
	 * @throws IOException
	 * @throws InvalidConnectionDetailsException
	 * 
	 */
	public abstract void loadConnectionDetailsFromFile(File file)
			throws CouldNotReadDatabaseConfigException, IOException, InvalidConnectionDetailsException;

	/**
	 * Reconnect if auto reconnect is active
	 * 
	 * @return {@code boolean} whether autoReconnect active
	 * 
	 * @throws SQLException
	 * 
	 */
	protected boolean autoReconnect() throws SQLException {
		if (autoReconnect)
			reconnect();
		return autoReconnect;
	}

	/**
	 * Reconnect to Database
	 * 
	 * @throws SQLException
	 * 
	 */
	public void reconnect() throws SQLException {
		if (isConnected()) {
			try {
				close();
				open();
			} catch (NoConectionStatusChangeException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}

	protected void connectionDetailsUpdate() throws InvalidConnectionDetailsException {
		try {
			reconnect();
		} catch (SQLException e) {
			throw new InvalidConnectionDetailsException("Unable to auto reconnect with the set connection values");
		}
	}

	/**
	 * Connected to Database?
	 * 
	 * @return Connection status to database as {@code boolean}
	 * 
	 */
	public boolean isConnected() {
		return !(con == null);
	}

	/**
	 * Fetch all rows of result from database
	 * 
	 * @param sql    SQL Command; Use {@code ?} instead of non constant Parameters
	 * @param params Parameters in Order to replace the {@code ?} symbols in
	 *               {@code sql}
	 * 
	 * @return {@link ArrayList} of rows; Each row is represented in a
	 *         {@link HashMap} mapping column names to values in form of
	 *         {@link DataElement DataElements}.
	 * @throws SQLException
	 * @throws NotConnectedException
	 * 
	 */
	public ArrayList<HashMap<String, DataElement>> getTable(String sql, Object... params)
			throws SQLException, NotConnectedException {

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
			for (int i = 0; i < params.length; i++) {
				if (params[i]==null) {
					statement.setString(i + 1, null);
					continue;
				}
				statement.setString(i + 1, params[i].toString());
			}

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
					HashMap<String, DataElement> currentrow = new HashMap<>();

					for (int collumid = 1; collumid <= sqlresult.getMetaData().getColumnCount(); collumid++)
						currentrow.put(sqlresult.getMetaData().getColumnName(collumid),
								new DataElement(sqlresult.getString(collumid)));

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
	 * Fetch all rows of result from database
	 * 
	 * @param sql SQL command; Use of {@code ?} instead of non constant Parameters
	 *            is not permitted
	 * 
	 * @return {@link ArrayList} of rows; Each row is represented in a
	 *         {@link HashMap} mapping column names to values in form of
	 *         {@link DataElement DataElements}.
	 * @throws SQLException
	 * @throws NotConnectedException
	 * 
	 */
	public ArrayList<HashMap<String, DataElement>> getTable(String sql) throws SQLException, NotConnectedException {
		return getTable(sql, new Object[] {});
	}

	/**
	 * Fetch only first row of result from database
	 * 
	 * @param sql    SQL Command; Use {@code ?} instead of non constant Parameters
	 * @param params Parameters in Order to replace the {@code ?} symbols in
	 *               {@code sql}
	 * 
	 * @return First row of result, represented in a {@link HashMap} mapping column
	 *         names to values in form of {@link DataElement DataElements}.
	 * @throws SQLException
	 * @throws NotConnectedException
	 * 
	 */
	public HashMap<String, DataElement> getFirstRow(String sql, Object... params)
			throws SQLException, NotConnectedException {

		ArrayList<HashMap<String, DataElement>> table = getTable(sql, params);

		if (table.isEmpty())
			return null;

		return table.get(0);
	}

	/**
	 * Fetch only first row of result from database
	 * 
	 * @param sql SQL command; Use of {@code ?} instead of non constant Parameters
	 *            is not permitted
	 * 
	 * @return First row of result, represented in a {@link HashMap} mapping column
	 *         names to values in form of {@link DataElement DataElements}.
	 * @throws SQLException
	 * @throws NotConnectedException
	 * 
	 */
	public HashMap<String, DataElement> getFirstRow(String sql) throws SQLException, NotConnectedException {
		return getFirstRow(sql, new Object[] {});
	}

	public boolean autoReconnectActive() {
		return autoReconnect;
	}

	public void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	public Connection getConnection() {
		return con;
	}

}

package de.pentagonlp.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.pentagonlp.database.DatabaseConnection;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

public class SqlLiteConnection extends DatabaseConnection {

	public SqlLiteConnection() throws InvalidConnectionDetailsException {
		super();
	}

	public SqlLiteConnection(String Host, boolean autoRecon) throws InvalidConnectionDetailsException {
		super(Host, "", "", "", "", autoRecon);
	}

	public SqlLiteConnection(String Host) throws InvalidConnectionDetailsException {
		super(Host, "", "", "", "");
	}

	@Override
	public Connection getConnection(String host, String port, String database, String username, String password)
			throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + host);
	}

}

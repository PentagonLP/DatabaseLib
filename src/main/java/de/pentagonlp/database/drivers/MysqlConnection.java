package de.pentagonlp.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.pentagonlp.database.DatabaseConnection;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;

public class MysqlConnection extends DatabaseConnection {

	public MysqlConnection() throws InvalidConnectionDetailsException {
		super();
	}

	public MysqlConnection(String Host, String Port, String Database, String Username, String Password,
			boolean autoRecon) throws InvalidConnectionDetailsException {
		super(Host, Port, Database, Username, Password, autoRecon);
	}

	public MysqlConnection(String Host, String Port, String Database, String Username, String Password)
			throws InvalidConnectionDetailsException {
		super(Host, Port, Database, Username, Password);
	}

	@Override
	public Connection getConnection(String host, String port, String database, String username, String password) throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useTimezone=true&serverTimezone=UTC",username, password);
	}

	

}

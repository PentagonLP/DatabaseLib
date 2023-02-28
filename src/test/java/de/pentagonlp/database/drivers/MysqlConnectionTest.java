package de.pentagonlp.database.drivers;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;
import de.pentagonlp.database.exceptions.NoConectionStatusChangeException;

public class MysqlConnectionTest {

	@Test
	public void connectTest() throws InvalidConnectionDetailsException, NoConectionStatusChangeException, SQLException {
		MysqlConnection mysql = new MysqlConnection();
		mysql.open();
		mysql.close();
	}
	
}

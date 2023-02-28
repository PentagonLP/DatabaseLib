package de.pentagonlp.database.drivers;

import java.io.File;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;
import de.pentagonlp.database.exceptions.NoConectionStatusChangeException;

class SqlLiteConnectionTest {

	@Test
	void openTest() throws InvalidConnectionDetailsException, NoConectionStatusChangeException, SQLException {
		SqlLiteConnection sqllite = new SqlLiteConnection("test.db");
		sqllite.open();
		sqllite.close();
		new File("test.db").delete();
	}

}

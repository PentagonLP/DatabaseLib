package de.pentagonlp.database.drivers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import de.pentagonlp.database.DataElement;
import de.pentagonlp.database.exceptions.InvalidConnectionDetailsException;
import de.pentagonlp.database.exceptions.NoConectionStatusChangeException;
import de.pentagonlp.database.exceptions.NotConnectedException;

/**
 * Test Class for {@link MysqlConnection}
 * 
 * @author PentagonLP
 * 
 */
public class MysqlConnectionTest {

	/**
	 * Creates a new {@link MysqlConnection}, creates a test database and tests its
	 * functionality
	 * 
	 */
	@Test
	public void connectTest() throws InvalidConnectionDetailsException, NoConectionStatusChangeException, SQLException,
			NotConnectedException {
		MysqlConnection mysql = new MysqlConnection();

		try {
			mysql.open();

			// Delete old and create new database
			mysql.getFirstRow("DROP DATABASE IF EXISTS databaselibtest");
			mysql.getFirstRow("CREATE DATABASE databaselibtest CHARACTER SET utf8 COLLATE utf8_general_ci");

			try {
				// Select database and create table
				mysql.getFirstRow("USE databaselibtest");
				mysql.getFirstRow(
						"CREATE TABLE `test` (`ID` int(11) NOT NULL AUTO_INCREMENT, `Data` varchar(16) DEFAULT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");

				// Test empty results
				assertEquals(mysql.getTable("SELECT * FROM test").isEmpty(), true,
						"getTable() fetched results from empty table");
				assertEquals(mysql.getFirstRow("SELECT * FROM test"), null,
						"getFirstRow() fetched results from empty table");

				// Insert some data
				mysql.getFirstRow(
						"INSERT INTO `test` (`ID`, `Data`) VALUES ('1', 'foobar'), ('2', '5'), ('3', '1'), ('4', NULL)");

				// Fetch the data and make sure all of it is fetched
				ArrayList<HashMap<String, DataElement>> tableresults = mysql
						.getTable("SELECT * FROM test ORDER BY ID ASC");
				assertEquals(tableresults.size(), 4, "getTable() more or less results then it should have ("
						+ tableresults.size() + " instead of 4)");

				// Test parsing
				assertEquals(tableresults.get(0).get("Data").toString(), "foobar",
						"getTable(): ID=1 (Data='foobar') failed to fetch String (without variable arguments)");
				assertEquals(tableresults.get(1).get("Data").toByte(), 5,
						"getTable(): ID=2 (Data=5) failed to cast to Byte");
				assertEquals(tableresults.get(1).get("Data").toInt(), 5,
						"getTable(): ID=2 (Data=5) failed to cast to Int");
				assertEquals(tableresults.get(1).get("Data").toLong(), 5,
						"getTable(): ID=2 (Data=5) failed to cast to Long");
				assertEquals(tableresults.get(1).get("Data").toFloat(), 5,
						"getTable(): ID=2 (Data=5) failed to cast to Float");
				assertEquals(tableresults.get(1).get("Data").toDouble(), 5,
						"getTable(): ID=2 (Data=5) failed to cast to Double");
				assertTrue(tableresults.get(2).get("Data").toBoolean(),
						"getTable(): ID=3 (Data=1) failed to cast to Boolean");
				assertTrue(tableresults.get(3).get("Data").isNull(),
						"getTable(): ID=4 (Data=NULL) should pass DataElement.isNull() check, but doesn't");

				// Test getTable() variable arguments
				tableresults = mysql.getTable("SELECT * FROM test WHERE ID=?", 1);
				assertEquals(tableresults.get(0).get("Data").toString(), "foobar",
						"getTable(): ID=1 (Data='foobar') failed to fetch String (with variable arguments)");

				// Test getFirstRow() data fetching
				assertEquals(mysql.getFirstRow("SELECT * FROM test WHERE ID=1").get("Data").toString(), "foobar",
						"getFirstRow(): ID=1 (Data='foobar') failed to fetch String (without variable arguments)");
				assertEquals(mysql.getFirstRow("SELECT * FROM test WHERE ID=?", 1).get("Data").toString(), "foobar",
						"getFirstRow(): ID=1 (Data='foobar') failed to fetch String (with variable arguments)");
				assertTrue(mysql.getFirstRow("SELECT * FROM test WHERE ID=4").get("Data").isNull(),
						"getFirstRow(): ID=4 (Data=NULL) is not 'null'");
			} finally {
				// Delete the test database again
				mysql.getFirstRow("DROP DATABASE databaselibtest");
			}
		} finally {
			mysql.close();
		}
	}

}

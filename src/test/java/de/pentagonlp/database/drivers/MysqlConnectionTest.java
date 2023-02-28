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

public class MysqlConnectionTest {

	@Test
	public void connectTest() throws InvalidConnectionDetailsException, NoConectionStatusChangeException, SQLException, NotConnectedException {
		MysqlConnection mysql = new MysqlConnection();
		
		try {
			mysql.open();
			mysql.getFirstRow("DROP DATABASE IF EXISTS databaselibtest");
		
			mysql.getFirstRow("CREATE DATABASE databaselibtest CHARACTER SET utf8 COLLATE utf8_general_ci");
		
			try {
				mysql.getFirstRow("USE databaselibtest");
		
				mysql.getFirstRow("CREATE TABLE `test` (`ID` int(11) NOT NULL AUTO_INCREMENT, `Data` varchar(16) DEFAULT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8");
				
				assertEquals(mysql.getTable("SELECT * FROM test").isEmpty(), true, "getTable() fetched results from empty table");
				assertEquals(mysql.getFirstRow("SELECT * FROM test"), null, "fastget() fetched results from empty table");
				
				mysql.getFirstRow("INSERT INTO `test` (`ID`, `Data`) VALUES ('1', 'foobar'), ('2', '5'), ('3', '1'), ('4', NULL)");
				
				ArrayList<HashMap<String, DataElement>> tableresults = mysql.getTable("SELECT * FROM test ORDER BY ID ASC");
				
				assertEquals(tableresults.size(), 4, "getTable() more or less results then it should have (" + tableresults.size() + " instead of 4)");
				
				assertEquals(tableresults.get(0).get("Data").toString(), "foobar", "getTable(): ID=1 (Data='foobar') failed to fetch String (without variable arguments)"); 
				assertEquals(tableresults.get(1).get("Data").toByte(), 5, "getTable(): ID=2 (Data=5) failed to cast to Byte");
				assertEquals(tableresults.get(1).get("Data").toInt(), 5, "getTable(): ID=2 (Data=5) failed to cast to Int");
				assertEquals(tableresults.get(1).get("Data").toLong(), 5, "getTable(): ID=2 (Data=5) failed to cast to Long");
				assertEquals(tableresults.get(1).get("Data").toFloat(), 5, "getTable(): ID=2 (Data=5) failed to cast to Float"); 
				assertEquals(tableresults.get(1).get("Data").toDouble(), 5, "getTable(): ID=2 (Data=5) failed to cast to Double");
				assertTrue(tableresults.get(2).get("Data").toBoolean(), "getTable(): ID=3 (Data=1) failed to cast to Boolean");
				assertTrue(tableresults.get(3).get("Data").isNull(), "getTable(): ID=4 (Data=NULL) should pass DataElement.isNull() check, but doesn't");
				
				tableresults = mysql.getTable("SELECT * FROM test WHERE ID=?", 1);
				
				assertEquals(tableresults.get(0).get("Data").toString(), "foobar", "getTable(): ID=1 (Data='foobar') failed to fetch String (with variable arguments)"); 
				
				assertEquals(mysql.getFirstRow("SELECT * FROM test WHERE ID=1").get("Data").toString(), "foobar", "fastget(): ID=1 (Data='foobar') failed to fetch String (without variable arguments)");
				assertEquals(mysql.getFirstRow("SELECT * FROM test WHERE ID=?",1).get("Data").toString(), "foobar", "fastget(): ID=1 (Data='foobar') failed to fetch String (with variable arguments)");	
				assertTrue(mysql.getFirstRow("SELECT * FROM test WHERE ID=4").get("Data").isNull(), "fastget(): ID=4 (Data=NULL) is not 'null'");
			
			} finally {
				mysql.getFirstRow("DROP DATABASE databaselibtest");
			}
		
		} finally {
			mysql.close();
		}
	}
	
}

package org.hibernate.gradle.mixedjdkissue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Test;

/**
 * @author Steve Ebersole
 */
public class RepositoryTest {
	@Test
	public void testIt() throws SQLException {
		// make sure we can use the Repository
		Repository.INSTANCE.store( new Object() );
		Connection c = DriverManager.getConnection( "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MVCC=TRUE" );
		System.out.println( "Connection : " + c );
		c.createStatement().execute(
				"CREATE ALIAS allEmployeeNames AS $$\n" +
						"import org.h2.tools.SimpleResultSet;\n" +
						"import java.sql.*;\n" +
						"@CODE\n" +
						"ResultSet allEmployeeNames() {\n" +
						"    SimpleResultSet rs = new SimpleResultSet();\n" +
						"    rs.addColumn(\"ID\", Types.INTEGER, 10, 0);\n" +
						"    rs.addColumn(\"FIRSTNAME\", Types.VARCHAR, 255, 0);\n" +
						"    rs.addColumn(\"LASTNAME\", Types.VARCHAR, 255, 0);\n" +
						"    rs.addRow(1, \"Steve\", \"Ebersole\");\n" +
						"    rs.addRow(1, \"Jane\", \"Doe\");\n" +
						"    rs.addRow(1, \"John\", \"Doe\");\n" +
						"    return rs;\n" +
						"}\n" +
						"$$"
		);
		c.close();
	}
}

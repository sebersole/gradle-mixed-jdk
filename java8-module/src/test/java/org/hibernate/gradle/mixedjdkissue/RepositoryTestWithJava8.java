package org.hibernate.gradle.mixedjdkissue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

/**
 * @author Steve Ebersole
 */
public class RepositoryTestWithJava8 {
	@Test
	public void testItWithJava8() throws SQLException {
		Repository.INSTANCE.store( new TemporalHolder() );
		Repository.INSTANCE.store( new InnerTemporalHolder() );

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

	private static class InnerTemporalHolder {
		private LocalDateTime localDateTime = LocalDateTime.now();
		private LocalDate localDate = LocalDate.now();
		private LocalTime localTime = LocalTime.now();
		private Instant instant = Instant.now();
		private ZonedDateTime zonedDateTime = ZonedDateTime.now();
		private OffsetDateTime offsetDateTime = OffsetDateTime.now();
		private OffsetTime offsetTime = OffsetTime.now();
		private Duration duration = Duration.of( 20, ChronoUnit.DAYS );
	}
}

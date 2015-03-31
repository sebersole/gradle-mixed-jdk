package org.hibernate.gradle.mixedjdkissue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Steve Ebersole
 */
public class TemporalHolder {
	private LocalDateTime localDateTime = LocalDateTime.now();
	private LocalDate localDate = LocalDate.now();
	private LocalTime localTime = LocalTime.now();
	private Instant instant = Instant.now();
	private ZonedDateTime zonedDateTime = ZonedDateTime.now();
	private OffsetDateTime offsetDateTime = OffsetDateTime.now();
	private OffsetTime offsetTime = OffsetTime.now();
	private Duration duration = Duration.of( 20, ChronoUnit.DAYS );
}

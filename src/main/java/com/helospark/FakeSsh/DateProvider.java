package com.helospark.FakeSsh;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

/**
 * Provides current date.
 * @author helospark
 */
@Component
public class DateProvider {

	public ZonedDateTime provideCurrentDate() {
		return ZonedDateTime.now();
	}
}

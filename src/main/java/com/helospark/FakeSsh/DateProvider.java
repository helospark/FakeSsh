package com.helospark.FakeSsh;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

@Component
public class DateProvider {

	public ZonedDateTime provideCurrentDate() {
		return ZonedDateTime.now();
	}
}

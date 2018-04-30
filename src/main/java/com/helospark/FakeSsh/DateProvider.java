package com.helospark.FakeSsh;

import java.time.ZonedDateTime;

import com.helospark.lightdi.annotation.Component;

@Component
public class DateProvider {

	public ZonedDateTime provideCurrentDate() {
		return ZonedDateTime.now();
	}
}

package com.helospark.FakeSsh;

import java.time.ZonedDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Provides the log file name.
 * @author helospark
 */
@Component
public class LogFileNameProvider {
	private String logPath;
	private String baseFileName;

	public LogFileNameProvider(@Value("${LOG_PATH}") String logPath, @Value("${BASE_FILE_NAME}") String baseFileName) {
		this.logPath = logPath;
		this.baseFileName = baseFileName;
	}

	public String provide(ZonedDateTime zonedDateTime) {
		return String.format(Locale.ENGLISH, "%s/%s_%d_%d_%d.log", logPath, baseFileName, zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth());
	}
}

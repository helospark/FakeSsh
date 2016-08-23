package com.helospark.FakeSsh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SynchronizedFileLogger {
	private DateProvider dateProvider;
	private BufferedWriter bufferedWriter;
	private String currentFileName = "";
	private LogFileNameProvider logFileNameProvider;

	@Autowired
	public SynchronizedFileLogger(DateProvider dateProvider, LogFileNameProvider logFileNameProvider) {
		this.dateProvider = dateProvider;
		this.logFileNameProvider = logFileNameProvider;
	}

	public synchronized void logToFile(String userName, String password, InetAddress inetAddress) throws IOException {
		ZonedDateTime zonedDateTime = dateProvider.provideCurrentDate();
		String newFileName = logFileNameProvider.provide(zonedDateTime);
		if (!currentFileName.equals(newFileName) || bufferedWriter == null) {
			bufferedWriter = new BufferedWriter(new FileWriter(newFileName, true));
			currentFileName = newFileName;
		}
		String logMessage = String.format("'%s':'%s':%s\n", userName, password, inetAddress.getHostAddress());
		bufferedWriter.append(logMessage);
		bufferedWriter.flush();
	}
}

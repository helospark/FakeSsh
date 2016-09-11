package com.helospark.FakeSsh.fakeos.filesystem;

import java.util.Arrays;
import java.util.Optional;

public class FakeFilesystem {
	private DirectoryEntry currentDirectory;
	private DirectoryEntry rootDirectory;

	public int createFile(String fileName) {
		return 0;
	}

	public void appendToFile(int fileHandle, byte[] data) {

	}

	public void goToDirectory(String targerDirectory) {
		DirectoryEntry savedCurrentDirectory = currentDirectory;
		try {
			tryGoToLocation(targerDirectory);
		} catch (Exception e) {
			this.currentDirectory = savedCurrentDirectory;
		}
	}

	private void tryGoToLocation(String targetDirectory) {
		if (targetDirectory.startsWith("/")) {
			goToRoot();
		}
		String[] pathSegments = targetDirectory.split("/");
		Arrays.stream(pathSegments)
				.forEach(segment -> goToSegment(segment));
	}

	private void goToRoot() {
		this.currentDirectory = rootDirectory;
	}

	private void goToSegment(String directoryName) {
		DirectoryEntry resultDirectory = currentDirectory.findChildrenByNameOrThrow(directoryName);
		if (resultDirectory == null) {
			throw new RuntimeException("Directory is not found");
		}
		this.currentDirectory = resultDirectory;
	}

	public DirectoryEntry getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(DirectoryEntry currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public void setRootDirectory(DirectoryEntry rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public Optional<DirectoryEntry> findFile(String name) {
		String[] parts = name.split("/");
		if (name.startsWith("/")) {
			return rootDirectory.findChildrenByName(parts, 1);
		} else {
			return currentDirectory.findChildrenByName(parts, 0);
		}
	}

}

package com.helospark.FakeSsh.fakeos.commands.ls;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.CommandArguments;
import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.FakeProgram;
import com.helospark.FakeSsh.fakeos.OsStream;
import com.helospark.FakeSsh.fakeos.filesystem.DirectoryEntry;
import com.helospark.FakeSsh.fakeos.filesystem.FakePermission;

@Component
public class FakeLs extends FakeProgram {

	@Override
	public int executeProgram(FakeOs os, OsStream osStream, CommandArguments arguments) {
		DirectoryEntry directory = os.getFileSystem().getCurrentDirectory();
		StringBuilder result = new StringBuilder();
		result.append(buildHeader(directory, arguments));
		List<DirectoryEntry> files = directory.getChildren();
		sortFiles(files, arguments);
		files.stream()
				.filter(file -> isFileOutputNeeded(file, arguments))
				.forEach(file -> result.append(buildRow(file, arguments)));
		osStream.getStandardOutput().writeLine(result.toString());
		return 0;
	}

	private String buildHeader(DirectoryEntry directory, CommandArguments arguments) {
		return "";
	}

	private void sortFiles(List<DirectoryEntry> files, CommandArguments arguments) {
		if (arguments.getShortNamedArguments().containsKey('c')) {
			files.sort((a, b) -> a.getCreationTime().compareTo(b.getCreationTime()));
		} else if (arguments.getShortNamedArguments().containsKey('S')) {
			files.sort((a, b) -> a.getSize().compareTo(b.getSize()));
		} else if (arguments.getShortNamedArguments().containsKey('t')) {
			files.sort((a, b) -> a.getModificationTime().compareTo(b.getModificationTime()));
		} else if (arguments.getShortNamedArguments().containsKey('t')) {
			files.sort((a, b) -> a.getAccessTime().compareTo(b.getAccessTime()));
		} else if (arguments.getShortNamedArguments().containsKey('X')) {
			files.sort((a, b) -> a.getName().compareTo(b.getName()));
		}
	}

	private boolean isFileOutputNeeded(DirectoryEntry file, CommandArguments arguments) {
		if (arguments.getShortNamedArguments().containsKey('a')) {
			return true;
		} else if (arguments.getShortNamedArguments().containsKey('A')) {
			return !(file.getName().equals(".") || file.getName().equals(".."));
		} else if (arguments.getShortNamedArguments().containsKey('B')) {
			return !file.getName().endsWith("~");
		} else {
			return !file.getName().startsWith(".");
		}
	}

	private String buildRow(DirectoryEntry file, CommandArguments arguments) {
		StringBuilder stringBuilder = new StringBuilder();
		if (arguments.getShortNamedArguments().containsKey('l')) {
			stringBuilder.append(buildPermissionString(file));
			stringBuilder.append(" ");
			stringBuilder.append(file.getOwner().getName());
			stringBuilder.append(" ");
			stringBuilder.append(file.getGroup().getName());
			stringBuilder.append(" ");
			// TODO: author
			stringBuilder.append(formatSize(file, arguments));
			stringBuilder.append(" ");
			stringBuilder.append(file.getCreationTime().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
			stringBuilder.append(" ");
			stringBuilder.append(file.getCreationTime().getDayOfMonth());
			stringBuilder.append(" ");
			stringBuilder.append(file.getCreationTime().getYear());
			stringBuilder.append("\t");
			stringBuilder.append(file.getName());
			stringBuilder.append("\r\n");
		} else {
			stringBuilder.append(file.getName());
			stringBuilder.append("\t");
		}
		return stringBuilder.toString();
	}

	private String formatSize(DirectoryEntry file, CommandArguments arguments) {
		char[] suffixes = { 'K', 'M', 'G', 'T' };
		Long fileSize = file.getSize();
		if (arguments.getShortNamedArguments().containsKey('h')) {
			int loopCount = 0;
			while (fileSize > 1000 && loopCount < suffixes.length) {
				fileSize /= 1000;
				loopCount++;
			}
			if (loopCount > 0) {
				return fileSize.toString() + suffixes[loopCount - 1];
			} else {
				return fileSize.toString();
			}
		} else {
			return fileSize.toString();
		}
	}

	private String buildPermissionString(DirectoryEntry file) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(file.isDirectory() ? 'd' : '-');
		stringBuilder.append(permissionChar(file.getOwnerPermission()));
		stringBuilder.append(permissionChar(file.getGroupPermission()));
		stringBuilder.append(permissionChar(file.getOtherPermission()));
		return stringBuilder.toString();
	}

	private String permissionChar(FakePermission permission) {
		char[] result = new char[3];
		result[0] = permission.hasReadPermission() ? 'r' : '-';
		result[1] = permission.hasWritePermission() ? 'w' : '-';
		result[2] = permission.hasExecutePermission() ? 'x' : '-';
		return new String(result);
	}

	@Override
	public boolean canHandle(String programName) {
		return programName.equals("ls");
	}
}

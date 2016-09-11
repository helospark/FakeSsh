package com.helospark.FakeSsh.fakeos.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.FakeOs;
import com.helospark.FakeSsh.fakeos.groups.FakeGroupFinder;

@Component
public class FakeFilesystemFactory {
	private String rootFolder;
	private FakeGroupFinder fakeGroupFinder;
	private FakeUserFinder fakeUserFinder;
	private FakePermissionFactory permissionFactory;

	@Autowired
	public FakeFilesystemFactory(@Value("${FAKE_FILESYSTEM_ROOT}") String rootFolder, FakeGroupFinder fakeGroupFinder, FakeUserFinder fakeUserFinder, FakePermissionFactory permissionFactory) {
		this.rootFolder = rootFolder;
		this.fakeGroupFinder = fakeGroupFinder;
		this.fakeUserFinder = fakeUserFinder;
		this.permissionFactory = permissionFactory;
	}

	public FakeFilesystem create(FakeOs fakeOs) {
		FakeFilesystem fakeFilesystem = new FakeFilesystem();
		initFakeFilesystemFromRealFileSystem(fakeOs, fakeFilesystem);
		return fakeFilesystem;
	}

	private void initFakeFilesystemFromRealFileSystem(FakeOs os, FakeFilesystem fakeFilesystem) {
		File rootFolderFile = new File(rootFolder);
		File[] listReadmeInRoot = rootFolderFile.listFiles((file) -> file.getName().equals("__README"));
		if (listReadmeInRoot.length == 0) {
			throw new RuntimeException("There is no README file in the root folder, the path might not refer to fake filesystem, exiting.");
		}
		List<File> listFiles = getChildrenFiles(rootFolderFile);
		DirectoryEntry rootDirectory = createRootDirectory(os);
		addChildrenToParent(rootDirectory, listFiles);
		fakeFilesystem.setCurrentDirectory(rootDirectory);
		fakeFilesystem.setRootDirectory(rootDirectory);
	}

	private List<File> getChildrenFiles(File rootFolderFile) {
		return Arrays.asList(rootFolderFile.listFiles((file) -> !file.getName().startsWith("__")));
	}

	private DirectoryEntry createRootDirectory(FakeOs os) {
		DirectoryEntry root = DirectoryEntry.builder()
				.withAccessTime(ZonedDateTime.now())
				.withBinary(false)
				.withCreationTime(ZonedDateTime.now().minusDays(30))
				.withDirectory(true)
				.withGroup(fakeGroupFinder.findGroupByName(os.getGroups(), "root"))
				.withOwner(fakeUserFinder.findUserByName(os.getUsers(), "root"))
				.withGroupPermission(permissionFactory.createFromString("r-x"))
				.withOtherPermission(permissionFactory.createFromString("r-x"))
				.withOwnerPermission(permissionFactory.createFromString("rwx"))
				.withName("")
				.withSize(4096)
				.build();
		root.setParent(root);
		return root;
	}

	private void addChildrenToParent(DirectoryEntry parent, List<File> files) {
		for (File file : files) {
			DirectoryEntry createdEntry = createFakeFile(parent, file);
			parent.addChildEntry(createdEntry);
			if (file.isDirectory()) {
				addChildrenToParent(createdEntry, getChildrenFiles(file));
			}
		}
	}

	private DirectoryEntry createFakeFile(DirectoryEntry parent, File file) {
		String fileContent = "";
		if (!file.isDirectory()) {
			fileContent = readFileContent(file);
		}
		return DirectoryEntry.builder()
				.withName(file.getName())
				.withBinary(isBinaryFile(fileContent))
				.withContent(fileContent)
				.withDirectory(file.isDirectory())
				.withSize(fileContent.getBytes().length)
				.withAccessTime(ZonedDateTime.now())
				.withCreationTime(ZonedDateTime.now().minusDays(30))
				.withModificationTime(ZonedDateTime.now().minusDays(10))
				.withGroup(parent.getGroup())
				.withGroupPermission(parent.getGroupPermission())
				.withOwnerPermission(parent.getOwnerPermission())
				.withOwner(parent.getOwner())
				.withOtherPermission(parent.getOtherPermission())
				.withSize(file.isDirectory() ? 4096 : fileContent.length())
				.withParent(parent)
				.build();
	}

	private String readFileContent(File file) {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(file.getPath()));
			return new String(encoded, "utf-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isBinaryFile(String content) {
		return content.startsWith("###BINARY");
	}
}

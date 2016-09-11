package com.helospark.FakeSsh.fakeos.filesystem;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryEntry {
	private ZonedDateTime creationTime;
	private long size;
	private ZonedDateTime modificationTime;
	private ZonedDateTime accessTime;
	private String name;
	private FakeUser owner;
	private FakeGroup group;
	private boolean isDirectory;
	private DirectoryEntry parent;
	private List<DirectoryEntry> children = new ArrayList<>();
	private FakePermission ownerPermission;
	private FakePermission groupPermission;
	private FakePermission otherPermission;
	private boolean isBinary;
	private String content;

	private DirectoryEntry(Builder builder) {
		this.creationTime = builder.creationTime;
		this.size = builder.size;
		this.modificationTime = builder.modificationTime;
		this.accessTime = builder.accessTime;
		this.name = builder.name;
		this.owner = builder.owner;
		this.group = builder.group;
		this.isDirectory = builder.isDirectory;
		this.children = builder.children;
		this.ownerPermission = builder.ownerPermission;
		this.groupPermission = builder.groupPermission;
		this.otherPermission = builder.otherPermission;
		this.isBinary = builder.isBinary;
		this.content = builder.content;
		this.parent = builder.parent;
	}

	public ZonedDateTime getCreationTime() {
		return creationTime;
	}

	public Long getSize() {
		return size;
	}

	public ZonedDateTime getModificationTime() {
		return modificationTime;
	}

	public ZonedDateTime getAccessTime() {
		return accessTime;
	}

	public String getName() {
		return name;
	}

	public FakeUser getOwner() {
		return owner;
	}

	public FakeGroup getGroup() {
		return group;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public FakePermission getOwnerPermission() {
		return ownerPermission;
	}

	public FakePermission getGroupPermission() {
		return groupPermission;
	}

	public FakePermission getOtherPermission() {
		return otherPermission;
	}

	public List<DirectoryEntry> getChildren() {
		return children;
	}

	public boolean isBinary() {
		return isBinary;
	}

	public String getContent() {
		return content;
	}

	public void addChildEntry(DirectoryEntry child) {
		children.add(child);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private ZonedDateTime creationTime;
		private long size;
		private ZonedDateTime modificationTime;
		private ZonedDateTime accessTime;
		private String name;
		private FakeUser owner;
		private FakeGroup group;
		private boolean isDirectory;
		private DirectoryEntry parent;
		private List<DirectoryEntry> children = new ArrayList<>();
		private FakePermission ownerPermission;
		private FakePermission groupPermission;
		private FakePermission otherPermission;
		private boolean isBinary;
		private String content;

		public Builder withCreationTime(ZonedDateTime creationTime) {
			this.creationTime = creationTime;
			return this;
		}

		public Builder withSize(long size) {
			this.size = size;
			return this;
		}

		public Builder withModificationTime(ZonedDateTime modificationTime) {
			this.modificationTime = modificationTime;
			return this;
		}

		public Builder withAccessTime(ZonedDateTime accessTime) {
			this.accessTime = accessTime;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withOwner(FakeUser owner) {
			this.owner = owner;
			return this;
		}

		public Builder withGroup(FakeGroup group) {
			this.group = group;
			return this;
		}

		public Builder withDirectory(boolean isDirectory) {
			this.isDirectory = isDirectory;
			return this;
		}

		public Builder withChildren(List<DirectoryEntry> children) {
			this.children = children;
			return this;
		}

		public Builder withOwnerPermission(FakePermission ownerPermission) {
			this.ownerPermission = ownerPermission;
			return this;
		}

		public Builder withGroupPermission(FakePermission groupPermission) {
			this.groupPermission = groupPermission;
			return this;
		}

		public Builder withOtherPermission(FakePermission otherPermission) {
			this.otherPermission = otherPermission;
			return this;
		}

		public Builder withBinary(boolean isBinary) {
			this.isBinary = isBinary;
			return this;
		}

		public Builder withContent(String content) {
			this.content = content;
			return this;
		}

		public Builder withParent(DirectoryEntry parent) {
			this.parent = parent;
			return this;
		}

		public DirectoryEntry build() {
			return new DirectoryEntry(this);
		}

	}

	public Optional<DirectoryEntry> findChildrenByName(String directoryName) {
		return children.stream()
				.filter(file -> file.getName().equals(directoryName))
				.findFirst();
	}

	public DirectoryEntry findChildrenByNameOrThrow(String directoryName) {
		return findChildrenByName(directoryName)
				.orElseThrow(() -> new RuntimeException("No folder by that name"));
	}

	public String toFullyQualifiedString() {
		StringBuilder stringBuilder = new StringBuilder("");
		buildFullyQualifiedName(stringBuilder);
		return stringBuilder.toString();
	}

	private void buildFullyQualifiedName(StringBuilder stringBuilder) {
		if (this != this.parent) {
			this.parent.buildFullyQualifiedName(stringBuilder);
		}
		stringBuilder.append(getName());
		if (isDirectory) {
			stringBuilder.append("/");
		}
	}

	public void setParent(DirectoryEntry parent) {
		this.parent = parent;
	}

	public Optional<DirectoryEntry> findChildrenByName(String[] parts, int index) {
		Optional<DirectoryEntry> result = findChildrenByName(parts[index]);
		if (!result.isPresent()) {
			return Optional.empty();
		} else {
			if (index == parts.length - 1) {
				return result;
			} else {
				return result.get().findChildrenByName(parts, index + 1);
			}
		}
	}
}

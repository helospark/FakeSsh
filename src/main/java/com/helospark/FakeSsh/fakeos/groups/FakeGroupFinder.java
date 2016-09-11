package com.helospark.FakeSsh.fakeos.groups;

import java.util.List;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.filesystem.FakeGroup;

@Component
public class FakeGroupFinder {

	public FakeGroup findGroupByName(List<FakeGroup> groups, String name) {
		return groups.stream()
				.filter(group -> group.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No group by that name"));
	}
}

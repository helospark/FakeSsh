package com.helospark.FakeSsh.fakeos.filesystem;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FakeUserFinder {

	public FakeUser findUserByName(List<FakeUser> users, String name) {
		return users.stream()
				.filter(user -> user.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No user by that name"));
	}

}

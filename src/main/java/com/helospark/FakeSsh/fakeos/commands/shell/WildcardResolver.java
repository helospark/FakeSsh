package com.helospark.FakeSsh.fakeos.commands.shell;

import org.springframework.stereotype.Component;

import com.helospark.FakeSsh.fakeos.FakeOs;

@Component
public class WildcardResolver {

	public String expandWildcards(FakeOs os, String command) {
		return command;
	}

}

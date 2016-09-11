package com.helospark.FakeSsh.fakeos.filesystem;

import org.springframework.stereotype.Component;

@Component
public class FakePermissionFactory {

	public FakePermission createFromString(String string) {
		FakePermission permission = new FakePermission();
		permission.setRead(string.indexOf('r') != -1);
		permission.setWrite(string.indexOf('w') != -1);
		permission.setExecute(string.indexOf('x') != -1);
		return permission;
	}

}

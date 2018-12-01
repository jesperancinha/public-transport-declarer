package com.jesperancinha.biscaje.model;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class UserTest {
	@Test
	public void testUserFillings() {
		final User user = User.builder().name("MyName").passwordString("PasswordString").lastLoggedIn("LastLoggedIn")
				.build();

		assertThat(user.getName()).isEqualTo("MyName");
		assertThat(user.getPasswordString()).isEqualTo("PasswordString");
		assertThat(user.getLastLoggedIn()).isEqualTo("LastLoggedIn");
	}

}
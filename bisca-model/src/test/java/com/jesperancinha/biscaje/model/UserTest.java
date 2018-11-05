package com.jesperancinha.biscaje.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created by joaofilipesabinoesperancinha on 20-04-16.
 */
public class UserTest {
	@Test
	public void testUserFillings() {
		final User user = User.builder().name("MyName").passwordString("PasswordString").lastLoggedIn("LastLoggedIn")
				.build();

		assertEquals(user.getName(), "MyName");
		assertEquals(user.getPasswordString(), "PasswordString");
		assertEquals(user.getLastLoggedIn(), "LastLoggedIn");
	}

}
package com.steelzack.biscaje.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by joaofilipesabinoesperancinha on 20-04-16.
 */
public class UserTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUserFillings() {
        final User user = User.builder().name("MyName").passwordString("PasswordString").lastLoggedIn("LastLoggedIn").build();

        assertEquals(user.getName(), "MyName");
        assertEquals(user.getPasswordString(), "PasswordString");
        assertEquals(user.getLastLoggedIn(), "LastLoggedIn");
    }

}
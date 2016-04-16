package com.steelzack.biscaje.model;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */
public class User {
    private String name;

    private String passwordString;

    private String lastLoggedIn;

    public String getName() {
        return name;
    }

    public String getPasswordString() {
        return passwordString;
    }

    public String getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPasswordString(String passwordString) {
        this.passwordString = passwordString;
    }

    public void setLastLoggedIn(String lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }
}


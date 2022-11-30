package com.jesperancinha.biscaje.exceptions;

public class DeckNotInitializedException extends Exception {

    @Override
    public String getMessage() {
        return "Deck has not been initialized yet!";
    }
}

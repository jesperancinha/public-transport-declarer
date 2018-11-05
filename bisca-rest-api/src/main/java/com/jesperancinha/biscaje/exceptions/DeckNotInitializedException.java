package com.jesperancinha.biscaje.exceptions;

/**
 * Created by joaofilipesabinoesperancinha on 27-04-16.
 */
public class DeckNotInitializedException extends Exception{

    @Override
    public String getMessage() {
        return "Deck has not been initialized yet!";
    }
}

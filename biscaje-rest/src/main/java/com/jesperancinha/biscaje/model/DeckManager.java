package com.jesperancinha.biscaje.model;

import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;

import java.util.Map;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public interface DeckManager {

    Map<SuitType, Suit> getDeckCards();

    DeckType getDeckType();

    void createAllCards();

    void shuffleCards() throws Exception;
}

package com.jesperancinha.biscaje.model;

import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;

import java.util.Map;

/**
 * Created by joaofilipesabinoesperancinha on 23-04-16.
 */
public interface Suit {
    Map<Integer, Card> createCards(SuitType suitType, DeckType deckType);

    Map<Integer, Card> getCards();

    void setCards(Map<Integer, Card> cards);
}

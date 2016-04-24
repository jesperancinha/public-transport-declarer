package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;

import java.util.Map;

/**
 * Created by joaofilipesabinoesperancinha on 23-04-16.
 */
public interface Suit {
    Map<Integer, Card> createCards(SuitType suitType, DeckType deckType);
}

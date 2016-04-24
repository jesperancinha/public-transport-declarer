package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;

import java.util.Map;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public interface DeckManager {
    Map<SuitType, Suit> createAllCards(DeckType deckType);

    Map<SuitType, Suit> getDeckCards();

    DeckType getDeckType();
}

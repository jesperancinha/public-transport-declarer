package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;

import java.util.Map;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class DeckManagerImpl implements DeckManager {

    final Map<SuitType, Suit> deckCards;

    public DeckManagerImpl(DeckType deckType) {
        deckCards = createAllCards(deckType);
    }

    private Map<SuitType,Suit> createAllCards(DeckType deckType) {
        return null;
    }
}

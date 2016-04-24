package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.CardType;
import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Card {
    DeckType getDeckType();

    CardType getCardType();

    SuitType getSuitType();

    Integer getCardValue();

    Integer getOrderNumber();

    String getImage();
}

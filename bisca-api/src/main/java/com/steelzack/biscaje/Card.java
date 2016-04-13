package com.steelzack.biscaje;

import com.steelzack.biscaje.enums.DeckType;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Card {
    Integer getCardValue();

    DeckType getDeckType();

    Integer getOrderNumber();

    String getImage();
}

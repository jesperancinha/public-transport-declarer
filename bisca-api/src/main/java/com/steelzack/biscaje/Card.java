package com.steelzack.biscaje;

import com.steelzack.biscaje.enums.SuitType;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Card {
    Integer getCardValue();

    SuitType getDeckType();

    Integer getOrderNumber();

    String getImage();
}

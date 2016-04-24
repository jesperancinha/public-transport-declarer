package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.CardType;
import com.steelzack.biscaje.game.enums.SuitType;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Card {
    Integer getCardValue();

    SuitType getDeckType();

    Integer getOrderNumber();

    String getImage();

    CardType getCardType();
}

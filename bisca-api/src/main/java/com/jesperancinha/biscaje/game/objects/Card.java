package com.jesperancinha.biscaje.game.objects;

import com.jesperancinha.biscaje.game.enums.CardType;
import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;

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

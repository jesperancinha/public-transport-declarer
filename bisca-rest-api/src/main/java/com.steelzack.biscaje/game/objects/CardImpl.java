package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.CardType;
import com.steelzack.biscaje.game.enums.SuitType;
import lombok.Getter;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Getter
public class CardImpl implements Card {
    private CardType cardType;

    public CardImpl(CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public Integer getCardValue() {
        return null;
    }

    @Override
    public SuitType getDeckType() {
        return null;
    }

    @Override
    public Integer getOrderNumber() {
        return null;
    }

    @Override
    public String getImage() {
        return null;
    }
}

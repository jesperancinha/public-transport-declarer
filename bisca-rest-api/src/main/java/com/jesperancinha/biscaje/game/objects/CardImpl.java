package com.jesperancinha.biscaje.game.objects;

import com.jesperancinha.biscaje.game.enums.CardType;
import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;
import lombok.Getter;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Getter
public class CardImpl implements Card {
    private CardType cardType;
    private SuitType suitType;
    private DeckType deckType;

    public CardImpl(CardType cardType, SuitType suitType, DeckType deckType) {
        this.cardType = cardType;
        this.suitType = suitType;
        this.deckType = deckType;
    }

    @Override
    public Integer getCardValue() {
        return cardType.getScore();
    }

    @Override
    public Integer getOrderNumber() {
        return cardType.getOrder();
    }

    @Override
    public String getImage() {
        return cardType.getImage();
    }
}

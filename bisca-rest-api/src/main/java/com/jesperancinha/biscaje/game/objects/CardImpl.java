package com.jesperancinha.biscaje.game.objects;

import com.jesperancinha.biscaje.game.enums.CardType;
import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;
import com.jesperancinha.biscaje.model.Card;
import lombok.Getter;

@Getter
public class CardImpl {
    private CardType cardType;
    private SuitType suitType;
    private DeckType deckType;

    public CardImpl(CardType cardType, SuitType suitType, DeckType deckType) {
        this.cardType = cardType;
        this.suitType = suitType;
        this.deckType = deckType;
    }

    public Integer getCardValue() {
        return cardType.getScore();
    }

    public Integer getOrderNumber() {
        return cardType.getOrder();
    }

    public String getImage() {
        return cardType.getImage();
    }
}

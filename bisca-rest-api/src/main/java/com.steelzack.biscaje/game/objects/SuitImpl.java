package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.CardType;
import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by joaofilipesabinoesperancinha on 23-04-16.
 */
@Getter
@Setter
class SuitImpl implements Suit {
    private Map<Integer, Card> cards;
    private DeckType deckType;
    private SuitType suitType;

    SuitImpl(final SuitType suitType, final DeckType deckType) {
        this.deckType = deckType;
        this.suitType = suitType;
        this.cards = createCards(this.suitType, this.deckType);
    }

    @Override
    public Map<Integer, Card> createCards(SuitType suitType, DeckType deckType) {
        return Arrays.stream( //
                CardType.values() //
        ).filter( //
                cardType -> cardType.getDeckType() == deckType //
        ).collect( //
                toMap( //
                        CardType::getOrder, //
                        cardType -> new CardImpl(cardType, suitType, deckType) //
                ) //
        );

    }
}

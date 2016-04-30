package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.exceptions.DeckNotInitializedException;
import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toMap;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Getter
@Builder
class DeckManagerImpl implements DeckManager {

    public static final int BOUND = 1000;
    public static final int INT = 500;

    private Map<SuitType, Suit> deckCards;
    private final DeckType deckType;

    @Override
    public void createAllCards() {
        deckCards = Arrays.stream( //
                SuitType.values() //
        ).filter( //
                suitType -> suitType.getDeckType() == deckType //
        ).collect( //
                toMap( //
                        suitType -> suitType,
                        suitType -> new SuitImpl(suitType, deckType)
                        //
                ) //
        );
    }

    @Override
    public void shuffleCards() throws DeckNotInitializedException {
        if (deckCards == null) {
            throw new DeckNotInitializedException();
        }

        deckCards.values().stream().forEach(
                suit -> {
                    final Map<Integer, Card> cards = suit.getCards();
                    final Map<Integer, Card> newCardSet = cards.values().stream().sorted( //
                            (card1, card2) -> new Random().nextInt(BOUND) - INT //
                    ).collect( //
                            toMap( //
                                    Card::getOrderNumber, //
                                    card -> card //
                            ) //
                    ); //
                    suit.setCards(newCardSet); //
                } //
        ); //
    }
}

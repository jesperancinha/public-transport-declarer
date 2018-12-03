package com.jesperancinha.biscaje.game.objects;

import com.jesperancinha.biscaje.game.enums.SuitType;
import com.jesperancinha.biscaje.model.Card;
import com.jesperancinha.biscaje.model.DeckManager;
import com.jesperancinha.biscaje.model.Suit;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.jesperancinha.biscaje.game.enums.DeckType.ITALIAN;
import static com.jesperancinha.biscaje.game.enums.DeckType.PORTUGUESE;
import static org.junit.Assert.assertEquals;

/**
 * Created by joaofilipesabinoesperancinha on 24-04-16.
 */
public class DeckManagerImplTest {
    @Test
    public void createAllCards_Portuguese() throws Exception {
        final DeckManager deckManager = DeckManagerImpl.builder().deckType(PORTUGUESE).build();
        deckManager.createAllCards();

        final Map<SuitType, Suit> result = deckManager.getDeckCards();

        assertEquals(4, result.size());
        assertEquals(PORTUGUESE, deckManager.getDeckType());
        Arrays.stream(
                SuitType.values()
        ).filter(
                suitType -> suitType.getDeckType() == PORTUGUESE
        ).forEach(
                suitType -> {
                    final Suit suit = result.get(suitType);
                    final Map<Integer, Card> cards = suit.getCards();
                    assertEquals(13, cards.size());
                    cards.values().stream().forEach(
                            card -> {
                                assertEquals(PORTUGUESE, card.getDeckType());
                                assertEquals(suitType, card.getSuitType());
                            }
                    );
                }
        );
    }

    @Test
    public void createAllCards_Italian() throws Exception {
        final DeckManager deckManager = DeckManagerImpl.builder().deckType(ITALIAN).build();
        deckManager.createAllCards();

        final Map<SuitType, Suit> result = deckManager.getDeckCards();

        assertEquals(4, result.size());
        assertEquals(ITALIAN, deckManager.getDeckType());
        Arrays.stream(
                SuitType.values()
        ).filter(
                suitType -> suitType.getDeckType() == ITALIAN
        ).forEach(
                suitType -> {
                    final Suit suit = result.get(suitType);
                    final Map<Integer, Card> cards = suit.getCards();
                    assertEquals(10, cards.size());
                    cards.values().stream().forEach(
                            card -> {
                                assertEquals(ITALIAN, card.getDeckType());
                                assertEquals(suitType, card.getSuitType());
                            }
                    );
                }
        );
    }
}
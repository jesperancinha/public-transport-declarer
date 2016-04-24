package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.CardType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.steelzack.biscaje.game.enums.DeckType.ITALIAN;
import static com.steelzack.biscaje.game.enums.DeckType.PORTUGUESE;
import static com.steelzack.biscaje.game.enums.SuitType.CLUBS_IT;
import static com.steelzack.biscaje.game.enums.SuitType.CLUBS_PT;
import static org.junit.Assert.assertEquals;

/**
 * Created by joaofilipesabinoesperancinha on 24-04-16.
 */
public class SuitImplTest {
    @Test
    public void createCards_Portuguese() throws Exception {
        final SuitImpl suit = new SuitImpl(CLUBS_PT, PORTUGUESE);

        final Map<Integer, Card> result = suit.getCards();

        assertEquals(CLUBS_PT, suit.getSuitType());
        assertEquals(PORTUGUESE, suit.getDeckType());
        assertEquals(13, result.size());
        Arrays.asList( //
                CardType.values() //
        ).stream().filter( //
                cardType -> cardType.getDeckType() == PORTUGUESE
        ).forEach( //
                cardType -> { //
                    assertEquals(cardType, result.get(cardType.getOrder()).getCardType()); //
                } //
        ); //
    }

    @Test
    public void createCards_Italian() throws Exception {
        final SuitImpl suit = new SuitImpl(CLUBS_IT, ITALIAN);

        final Map<Integer, Card> result = suit.getCards();

        assertEquals(CLUBS_IT, suit.getSuitType());
        assertEquals(ITALIAN, suit.getDeckType());
        assertEquals(10, result.size());
        Arrays.asList( //
                CardType.values() //
        ).stream().filter( //
                cardType -> cardType.getDeckType() == ITALIAN
        ).forEach( //
                cardType -> {
                    assertEquals(cardType, result.get(cardType.getOrder()).getCardType());
                }
        );
    }
}
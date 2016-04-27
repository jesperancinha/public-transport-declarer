package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Getter
@Builder
public class DeckManagerImpl implements DeckManager {
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
                        suitType ->  new SuitImpl(suitType, deckType)
                       //
                ) //
        );
    }
}

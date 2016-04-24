package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Getter
public class DeckManagerImpl implements DeckManager {
    final Map<SuitType, Suit> deckCards;

    public DeckManagerImpl(DeckType deckType) {
        deckCards = createAllCards(deckType);
    }

    @Override
    public Map<SuitType, Suit> createAllCards(DeckType deckType) {
       return Arrays.stream( //
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

package com.jesperancinha.biscaje.game.enums;

import lombok.Getter;

import static com.jesperancinha.biscaje.game.enums.DeckType.ITALIAN;
import static com.jesperancinha.biscaje.game.enums.DeckType.PORTUGUESE;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
@Getter
public enum SuitType {
    CLUBS_PT(PORTUGUESE),
    HEARTS_PT(PORTUGUESE),
    DIAMONS_PT(PORTUGUESE),
    SPADES_PT(PORTUGUESE),
    COINS_IT(ITALIAN),
    SWORDS_IT(ITALIAN),
    CUPS_IT(ITALIAN),
    CLUBS_IT(ITALIAN);

    private DeckType deckType;

    SuitType(DeckType deckType) {
        this.deckType = deckType;
    }
}

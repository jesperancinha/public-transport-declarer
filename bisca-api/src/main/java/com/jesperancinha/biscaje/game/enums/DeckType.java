package com.jesperancinha.biscaje.game.enums;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public enum DeckType {
    PORTUGUESE(3),
    ITALIAN(3);

    private Integer cardsPerPlayer;

    DeckType(final Integer cardsPerPlayer)
    {
        this.cardsPerPlayer = cardsPerPlayer;
    }

    public Integer getCardsPerPlayer() {
        return cardsPerPlayer;
    }
}

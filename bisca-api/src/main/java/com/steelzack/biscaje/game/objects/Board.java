package com.steelzack.biscaje.game.objects;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Board {
    Player getCurrentPlayer();

    List<Card> getPlayerCards(Player player);

    Card getTrunfo();

    void createFullDeck();
}

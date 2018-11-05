package com.jesperancinha.biscaje.game.objects;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Board {
    void orderPlayers(List<Player> players);

    List<Player> getPlayers();

    Player getCurrentPlayer();

    List<Card> getPlayerCards(Player player);

    Card getTrunfo();

    void createFullDeck();
}

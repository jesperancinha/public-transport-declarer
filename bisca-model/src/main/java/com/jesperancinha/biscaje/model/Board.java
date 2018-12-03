package com.jesperancinha.biscaje.model;

import java.util.List;

public interface Board {
    void orderPlayers(List<Player> players);

    List<Player> getPlayers();

    Player getCurrentPlayer();

    List<Card> getPlayerCards(Player player);

    Card getTrunfo();

    void createFullDeck();
}

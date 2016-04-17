package com.steelzack.biscaje.game.objects;

import java.util.Collections;
import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class BoardImpl implements Board {

    private final DeckManager deckManager;

    private List<Player> players;

    private Player currentPlayer;

    public BoardImpl(final List<Player> players, final DeckManager deckManager) {
        this.deckManager = deckManager;
        orderPlayers(players);
        this.players = players;
        final int size = this.players.size();
        final Player firstPlayer = this.players.get(0);
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                this.players.get(i).setNextPlayer(firstPlayer);
            } else {
                this.players.get(i).setNextPlayer(this.players.get(i + 1));
            }
        }
        this.currentPlayer = firstPlayer;
    }

    @Override
    public void orderPlayers(List<Player> players) {
        Collections.sort(players, (player1, player2) -> player1.getOrderId().compareTo(player2.getOrderId()));
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public List<Card> getPlayerCards(Player player) {
        return null;
    }

    @Override
    public Card getTrunfo() {
        return null;
    }

    @Override
    public void createFullDeck() {

    }
}

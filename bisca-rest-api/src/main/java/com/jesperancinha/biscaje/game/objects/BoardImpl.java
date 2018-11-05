package com.jesperancinha.biscaje.game.objects;

import com.jesperancinha.biscaje.game.enums.DeckType;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */

@Getter
public class BoardImpl implements Board {

    private DeckManager deckManager;

    private final DeckType deckType;

    private List<Player> players;

    private Player currentPlayer;

    private Card trunfo;

    public BoardImpl(final List<Player> players, final DeckType deckType) {
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
        this.deckType = deckType;
        createFullDeck();
    }

    @Override
    public void orderPlayers(List<Player> players) {
        Collections.sort(players, (player1, player2) -> player1.getOrderId().compareTo(player2.getOrderId()));
    }

    @Override
    public List<Card> getPlayerCards(Player player) {
        return null;
    }

    @Override
    public Card getTrunfo() {
        return trunfo;
    }

    @Override
    public void createFullDeck() {
        deckManager = DeckManagerImpl.builder().deckType(deckType).build();
    }
}

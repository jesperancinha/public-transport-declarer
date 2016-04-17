package com.steelzack.biscaje.game.objects;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
public class PlayerImpl implements Player {
    private Integer orderId;

    private String playerName;

    private Player nextPlayer;

    public PlayerImpl(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String getPlayerName() {
        return null;
    }

    @Override
    public Integer getPlayerPoints() {
        return null;
    }

    @Override
    public List<Card> getPlayerCards() {
        return null;
    }

    @Override
    public Integer getOrderId() {
        return orderId;
    }

    @Override
    public Player getNextPlayer() {
        return nextPlayer;
    }

    @Override
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
}

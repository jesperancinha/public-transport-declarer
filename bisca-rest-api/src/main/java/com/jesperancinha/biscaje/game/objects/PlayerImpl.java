package com.jesperancinha.biscaje.game.objects;

import lombok.Getter;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 17-04-16.
 */
@Getter
public class PlayerImpl implements Player {
    private Integer orderId;

    private String playerName;

    private Player nextPlayer;

    PlayerImpl(String playerName) {
        this.playerName = playerName;
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
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
}

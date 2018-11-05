package com.jesperancinha.biscaje.game.objects;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Player {
    String getPlayerName();

    Integer getPlayerPoints();

    List<Card> getPlayerCards();

    Integer getOrderId();

    Player getNextPlayer();

    void setOrderId(Integer orderId);

    void setNextPlayer(Player nextPlayer);
}

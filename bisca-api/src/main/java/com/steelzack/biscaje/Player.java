package com.steelzack.biscaje;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public interface Player {
    String getPlayerName();

    Integer getPlayerPoints();

    List<Card> getPlayerCards();
}

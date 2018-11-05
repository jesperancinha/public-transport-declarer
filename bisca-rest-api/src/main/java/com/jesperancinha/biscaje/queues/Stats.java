package com.jesperancinha.biscaje.queues;

import com.jesperancinha.biscaje.game.objects.Player;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 06-07-16.
 */
@Getter
@Builder
public class Stats implements Serializable {

    private static final long serialVersionUID = 701512264343070928L;

    private final Integer nPlayers;

    private final List<Player> players;

    private final Long epochTimeSpent;
}

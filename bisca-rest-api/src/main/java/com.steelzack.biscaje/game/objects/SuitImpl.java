package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.SuitType;

/**
 * Created by joaofilipesabinoesperancinha on 23-04-16.
 */
public class SuitImpl implements Suit {
    private SuitType type;

    public SuitImpl(final SuitType type)
    {
        this.type = type;
    }

    @Override
    public SuitType getSuit() {
        return type;
    }
}

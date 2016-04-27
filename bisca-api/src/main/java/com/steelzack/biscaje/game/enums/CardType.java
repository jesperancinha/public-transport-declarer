package com.steelzack.biscaje.game.enums;

import lombok.Getter;

import static com.steelzack.biscaje.game.enums.DeckType.ITALIAN;
import static com.steelzack.biscaje.game.enums.DeckType.PORTUGUESE;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
@Getter
public enum CardType {
    NUMERIC_ACE_PT_(1, 11, PORTUGUESE, null), //
    NUMERIC_2_PT_(2, 0, PORTUGUESE, null), //
    NUMERIC_3_PT_(3, 0, PORTUGUESE, null), //
    NUMERIC_4_PT_(4, 0, PORTUGUESE, null), //
    NUMERIC_5_PT_(5, 0, PORTUGUESE, null), //
    NUMERIC_6_PT_(6, 0, PORTUGUESE, null), //
    NUMERIC_7_PT_(7, 10, PORTUGUESE, null), //
    NUMERIC_8_PT_(8, 0, PORTUGUESE, null), //
    NUMERIC_9_PT_(9, 0, PORTUGUESE, null), //
    NUMERIC_10_PT_(10, 0, PORTUGUESE, null), //
    NUMERIC_Q_PT_(11, 2, PORTUGUESE, null), //
    NUMERIC_J_PT_(12, 3, PORTUGUESE, null), //
    NUMERIC_K_PT_(13, 4, PORTUGUESE, null), //
    NUMERIC_ACE_IT_(1, 11, ITALIAN, null), //
    NUMERIC_2_IT_(2, 0, ITALIAN, null), //
    NUMERIC_3_IT_(3, 10, ITALIAN, null), //
    NUMERIC_4_IT_(4, 0, ITALIAN, null), //
    NUMERIC_5_IT_(5, 0, ITALIAN, null), //
    NUMERIC_6_IT_(6, 0, ITALIAN, null), //
    NUMERIC_7_IT_(7, 0, ITALIAN, null), //
    NUMERIC_JACK_IT_(8, 0, ITALIAN, null), //
    NUMERIC_KNIGHT_IT_(9, 0, ITALIAN, null), //
    NUMERIC_KING_IT_(10, 0, ITALIAN, null); //

    private final int order;
    private final int score;
    private final DeckType deckType;
    private String image;

    CardType( //
              final int order, //
              final int score, //
              final DeckType deckType, //
              String image) { //

        this.order = order;
        this.score = score;
        this.deckType = deckType;
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}

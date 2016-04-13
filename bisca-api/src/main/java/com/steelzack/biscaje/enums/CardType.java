package com.steelzack.biscaje.enums;

/**
 * Created by joaofilipesabinoesperancinha on 13-04-16.
 */
public enum CardType {
    NUMERIC_ACE_PT_(1, 11, DeckType.PORTUGUESE), //
    NUMERIC_2_PT_(2, 0, DeckType.PORTUGUESE), //
    NUMERIC_3_PT_(3, 0, DeckType.PORTUGUESE), //
    NUMERIC_4_PT_(4, 0, DeckType.PORTUGUESE), //
    NUMERIC_5_PT_(5, 0, DeckType.PORTUGUESE), //
    NUMERIC_6_PT_(6, 10, DeckType.PORTUGUESE), //
    NUMERIC_7_PT_(7, 0, DeckType.PORTUGUESE), //
    NUMERIC_8_PT_(8, 0, DeckType.PORTUGUESE), //
    NUMERIC_9_PT_(9, 0, DeckType.PORTUGUESE), //
    NUMERIC_10_PT_(10, 0, DeckType.PORTUGUESE), //
    NUMERIC_Q_PT_(2, 0, DeckType.PORTUGUESE), //
    NUMERIC_J_PT_(3, 0, DeckType.PORTUGUESE), //
    NUMERIC_K_PT_(4, 0, DeckType.PORTUGUESE); //

    private final int order;
    private final int value;
    private final DeckType portuguese;

    CardType( //
              final int order, //
              final int value, //
              final DeckType portuguese //
    ) { //

        this.order = order;
        this.value = value;
        this.portuguese = portuguese;
    }
}

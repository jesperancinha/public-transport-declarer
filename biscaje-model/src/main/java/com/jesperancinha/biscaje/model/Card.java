package com.jesperancinha.biscaje.model;

import com.jesperancinha.biscaje.game.enums.CardType;
import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Setter
@Builder
@Entity
@Proxy(lazy = false)
@Table(name = "Card")
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private String id;

    private CardType cardType;
    private SuitType suitType;
    private DeckType deckType;

    public Card(CardType cardType, SuitType suitType, DeckType deckType) {
        this.cardType = cardType;
        this.suitType = suitType;
        this.deckType = deckType;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public SuitType getSuitType() {
        return suitType;
    }

    public DeckType getDeckType() {
        return deckType;
    }

    @Transient
    public Integer getOrderNumber() {
        return cardType.getOrder();
    }
}

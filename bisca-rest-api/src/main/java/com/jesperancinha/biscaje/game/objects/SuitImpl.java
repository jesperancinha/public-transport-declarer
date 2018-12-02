package com.jesperancinha.biscaje.game.objects;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;

import com.jesperancinha.biscaje.game.enums.CardType;
import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class SuitImpl implements Suit {
	private Map<Integer, Card> cards;
	private DeckType deckType;
	private SuitType suitType;

	SuitImpl(final SuitType suitType, final DeckType deckType) {
		this.deckType = deckType;
		this.suitType = suitType;
		this.cards = createCards(this.suitType, this.deckType);
	}

	@Override
	public Map<Integer, Card> createCards(SuitType suitType, DeckType deckType) {
		return Arrays.stream(
				CardType.values()
		).filter(
				cardType -> cardType.getDeckType() == deckType
		).collect(
				toMap(
						CardType::getOrder,
						cardType -> new CardImpl(cardType, suitType, deckType)
				)
		);

	}
}

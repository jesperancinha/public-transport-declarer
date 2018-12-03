package com.jesperancinha.biscaje.game.objects;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;

import com.jesperancinha.biscaje.exceptions.DeckNotInitializedException;
import com.jesperancinha.biscaje.game.enums.DeckType;
import com.jesperancinha.biscaje.game.enums.SuitType;
import com.jesperancinha.biscaje.model.Card;
import com.jesperancinha.biscaje.model.DeckManager;
import com.jesperancinha.biscaje.model.Suit;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class DeckManagerImpl implements DeckManager {

	public static final int BOUND = 1000;
	public static final int INT = 500;

	private Map<SuitType, Suit> deckCards;
	private final DeckType deckType;

	@Override
	public void createAllCards() {
		deckCards = Arrays.stream(
				SuitType.values()
		).filter(suitType -> suitType.getDeckType() == deckType
		).collect(toMap(suitType -> suitType,
				suitType -> new SuitImpl(suitType, deckType)));
	}

	@Override
	public void shuffleCards() throws DeckNotInitializedException {
		if (deckCards == null) {
			throw new DeckNotInitializedException();
		}

		deckCards.values().forEach(
				suit -> {
					final Map<Integer, Card> cards = suit.getCards();
					final Map<Integer, Card> newCardSet = cards.values().stream().collect(
							toMap(Card::getOrderNumber, card -> card)
					);
					suit.setCards(newCardSet);
				}
		);
	}
}

package com.steelzack.biscaje.game.objects;

import com.steelzack.biscaje.game.enums.CardType;
import com.steelzack.biscaje.game.enums.DeckType;
import com.steelzack.biscaje.game.enums.SuitType;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.steelzack.biscaje.game.enums.CardType.NUMERIC_K_PT_;
import static com.steelzack.biscaje.game.enums.DeckType.PORTUGUESE;
import static com.steelzack.biscaje.game.enums.SuitType.CLUBS_IT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by joaofilipesabinoesperancinha on 24-04-16.
 */
public class CardSteps {
    private CardType cardType;
    private SuitType suitType;
    private DeckType deckType;

    private CardImpl cardImpl;

    @Before
    public void setUp() {
        cardType = NUMERIC_K_PT_;
        suitType = CLUBS_IT;
        deckType = PORTUGUESE;
    }


    @Given("^I know the card type is \"([^\"]*)\"$")
    public void i_know_the_card_type_is(String cardType) throws Throwable {
    this.cardType = CardType.valueOf(cardType);

    }

    @Given("^I know the suit type is \"([^\"]*)\"$")
    public void i_know_the_suit_type_is(String suitType) throws Throwable {
        this.suitType = SuitType.valueOf(suitType);
    }

    @Given("^I know the deck type is \"([^\"]*)\"$")
    public void i_know_the_deck_type_is(String deckType) throws Throwable {
        this.deckType = DeckType.valueOf(deckType);
    }

    @Then("^the result should be king of clubs from a portuguese deck$")
    public void the_result_should_be_king_of_clubs_from_a_portuguese_deck() throws Throwable {
        assertEquals(cardType, cardImpl.getCardType());
        assertEquals(suitType, cardImpl.getSuitType());
        assertEquals(deckType, cardImpl.getDeckType());
    }

    @When("^I create it with a \"([^\"]*)\" deck, with a \"([^\"]*)\" suit and a \"([^\"]*)\" type$")
    public void i_create_it_with_a_deck_with_a_suit_and_a_type(String deckName, String suitName, String cardName) throws Throwable {
        final DeckType deckType = DeckType.valueOf(deckName);
        final SuitType suitType = SuitType.valueOf(suitName);
        final CardType cardType = CardType.valueOf(cardName);
        cardImpl = new CardImpl(cardType, suitType, deckType);
        assertNotNull(cardImpl.getCardType());
        assertNotNull(cardImpl.getSuitType());
        assertNotNull(cardImpl.getDeckType());
    }
}

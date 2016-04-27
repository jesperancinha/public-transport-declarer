Feature: CardImpl
  As a user
  I just want to create a card

  Scenario: Create a card
    Given I know the card type is "NUMERIC_ACE_PT_"
    And I know the suit type is "CLUBS_PT"
    And I know the deck type is "PORTUGUESE"
    When I create it with a "PORTUGUESE" deck, with a "CLUBS_PT" suit and a "NUMERIC_ACE_PT_" type
    Then the result should be king of clubs from a portuguese deck

  Scenario: Create a card
    Given I know the card type is "NUMERIC_JACK_IT_"
    And I know the suit type is "CLUBS_IT"
    And I know the deck type is "ITALIAN"
    When I create it with a "ITALIAN" deck, with a "CLUBS_IT" suit and a "NUMERIC_JACK_IT_" type
    Then the result should be king of clubs from a portuguese deck

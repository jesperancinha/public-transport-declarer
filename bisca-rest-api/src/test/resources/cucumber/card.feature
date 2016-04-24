Feature: CardImpl
  As a user
  I just want to create a card

  Scenario: Create a card
    Given I know the card type
    And I know the suit type
    And I know the deck type
    When I create it with a portuguese deck, with a clubs suit and a kings type
    Then the result should be king of clubs from a portuguese deck

  Scenario: Create another card
    Given I know the card type
    And I know the suit type
    And I know the deck type
    When I create it with a portuguese deck, with a clubs suit and a kings type
    Then the result should be king of clubs from a portuguese deck

  Scenario: Create yet another card
    Given I know the card type
    And I know the suit type
    And I know the deck type
    When I create it with a portuguese deck, with a clubs suit and a kings type
    Then the result should be king of clubs from a portuguese deck
Feature: Token
  Background: Registration of customer and merchant
  Scenario: Successful Token Generation
    Given the customer has no tokens
    When the customer tries to spend a token and gets an error
    Then the customer can request new tokens
    And the customer can spend the new token




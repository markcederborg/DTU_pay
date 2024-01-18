Feature: Merchant Registration
  Scenario: Successful Registration
    Given a merchant with name "Mike" "Merchant" and cvr "654321-1234"
    And the merchant isn't registered with DTUpay and have an empty id
    And the merchant has a bank account with balance 1000
    When merchant registers for DTUpay using bankId
    Then the merchant is registered has a non empty id
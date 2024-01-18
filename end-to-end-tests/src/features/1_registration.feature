Feature: Registration
  Scenario: Successful Customer Registration
    Given a customer with name "Conrad" "Customer" and cpr "1dfgffdsd56-1239"
    And the customer isn't registered with DTUpay and have an empty id
    And the customer has a bank account with balance 1000
    When customer registers for DTUpay
    Then the customer is registered with non-empty id

    Scenario: Successful Merchant Registration
    Given a merchant with name "Mike" "Merchant" and cvr "6gcsffxff41-1232"
    And the merchant isn't registered with DTUpay and have an empty id
    And the merchant has a bank account with balance 1000
    When merchant registers for DTUpay
    Then the merchant is registered with non-empty id


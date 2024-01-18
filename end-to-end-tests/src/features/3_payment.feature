Feature: Payment
  Background: Registration of customer and merchant
    The customer has 1000 kr at the bank and the merchant has 1000 kr at the bank
    and customer has tokens available
  Scenario: Successful Payment at Merchant
    When the merchant initiates a payment for 100 kr by the customer for "flowers"
    Then the payment is successful
    And the balance of the customer at the bank is 900 kr
    And the balance of the merchant at the bank is 1100 kr


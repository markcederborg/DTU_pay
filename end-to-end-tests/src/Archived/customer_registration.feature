Feature: Customer Registration
  Scenario: Successful Registration
    Given a customer with name "Conrad" "Customer" and cpr "123456-1234"
    And the customer isn't registered with DTUpay and have an empty id
    And the customer has a bank account with balance 1000
    When customer registers for DTUpay using bankId
    Then the customer is registered has a non empty id

 
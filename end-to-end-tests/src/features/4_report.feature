Feature: Report
  Background: There exists transactions in the system
  Scenario: Successful Payment Report
    When the manager requests a report of payments
    Then the report is generated
  Scenario: Successful Merchant Payment Report
    When the manager requests a report of payments
    Then the report is generated



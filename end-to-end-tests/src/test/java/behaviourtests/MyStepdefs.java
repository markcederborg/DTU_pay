package behaviourtests;

import dtupay.dto.*;
import exceptions.BankGetAccountException;
import exceptions.BankRegistrationException;
import exceptions.ReportFetchingException;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utils.Adapter;
import utils.Factory;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyStepdefs {
    static Adapter adapter = new Factory().getAdapter();
    ArrayList report = new ArrayList<Payment>();

    @BeforeAll
    public static void tearUp() throws Exception {
        try {
            adapter.customerApp.retireAccount();
        } catch (Exception e) {
            System.out.println("Customer not registered");
        }
        try {
            adapter.merchantApp.retireAccount();
        } catch (Exception e) {
            System.out.println("Merchant not registered");
        }

        try {
            adapter.customerApp.retireBank();
        } catch (Exception e) {
            System.out.println("Customer bank not registered");
        }

        try {
            adapter.merchantApp.retireBank();
        } catch (Exception e) {
            System.out.println("Merchant bank not registered");
        }
    }

    @Given("a customer with name {string} {string} and cpr {string}")
    public void aCustomerWithNameAndCpr(String arg0, String arg1, String arg2) {
        adapter.customerApp.init(arg0, arg1, arg2);
    }

    @And("the customer isn't registered with DTUpay and have an empty id")
    public void theCustomerIsnTRegisteredWithDTUpayAndHaveAnEmptyId() {
        assertNull(adapter.customerApp.getAccount().getAccountId());
    }

    @And("the customer has a bank account with balance {int}")
    public void theCustomerHasABankAccountWithBalance(int arg0) throws BankRegistrationException {
        adapter.customerApp.registerBank(arg0);
    }

    @When("customer registers for DTUpay")
    public void customerRegistersForDTUpay() throws Exception {
        adapter.customerApp.registerAccount();
    }

    @Then("the customer is registered with non-empty id")
    public void theCustomerIsRegisteredHasANonEmptyId() {
        System.out.println("customer id: " + adapter.customerApp.getAccount().getAccountId());
        assertNotNull(adapter.customerApp.getAccount().getAccountId());
    }

    @Given("a merchant with name {string} {string} and cvr {string}")
    public void aMerchantWithNameAndCvr(String arg0, String arg1, String arg2) {
        adapter.merchantApp.init(arg0, arg1, arg2);
    }

    @And("the merchant isn't registered with DTUpay and have an empty id")
    public void theMerchantIsnTRegisteredWithDTUpayAndHaveAnEmptyId() {
        assertNull(adapter.merchantApp.getAccount().getAccountId());
    }

    @And("the merchant has a bank account with balance {int}")
    public void theMerchantHasABankAccountWithBalance(int arg0) {
        adapter.merchantApp.registerBank(arg0);
    }

    @When("merchant registers for DTUpay")
    public void merchantRegistersForDTUpay() throws Exception {
        adapter.merchantApp.registerAccount();
    }

    @Then("the merchant is registered with non-empty id")
    public void theMerchantIsRegisteredHasANonEmptyId() {
        assertNotNull(adapter.merchantApp.getAccount().getAccountId());
    }

    @When("the merchant initiates a payment for {int} kr by the customer for {string}")
    public void theMerchantInitiatesAPaymentForKrByTheCustomer(int cost, String description) throws Exception {
        String token = adapter.customerApp.spendToken();

        String merchantBank = adapter.merchantApp.getAccount().getBankId();

        if (merchantBank == null) {
            throw new Exception("Merchant bank is null");
        }

        Payment payment = Payment.builder()
                .merchantId(merchantBank)
                .token(token)
                .amount(cost)
                .description(description)
                .build();
        adapter.merchantApp.initiatePayment(payment);
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() {
    }

    @And("the balance of the customer at the bank is {int} kr")
    public void theBalanceOfTheCustomerAtTheBankIsKr(int arg0) throws BankGetAccountException {
        BigDecimal expected = BigDecimal.valueOf(arg0);
        BigDecimal balance = adapter.customerApp.getBankAccount().getBalance();
        Assert.assertEquals(expected, balance);
    }

    @And("the balance of the merchant at the bank is {int} kr")
    public void theBalanceOfTheMerchantAtTheBankIsKr(int arg0) {
        BigDecimal expected = BigDecimal.valueOf(arg0);
        BigDecimal balance = adapter.merchantApp.getBankAccount().getBalance();

        Assert.assertEquals(expected, balance);
    }

    @Given("the customer has no tokens")
    public void theCustomerHasNoTokens() {
        Assert.assertTrue(adapter.customerApp.getTokens().isEmpty());
    }

    @When("the customer tries to spend a token and gets an error")
    public void theCustomerTriesToSpendATokenAndGetsAnError() {
        String token = null;
        try {
            token = adapter.customerApp.spendToken();
        } catch (Exception e) {
            System.out.println(e);
        }
        Assert.assertNull(token);
    }

    @Then("the customer can request new tokens")
    public void theCustomerCanRequestNewTokens() throws Exception {
        adapter.customerApp.requestTokens();
    }

    @And("the customer can spend the new token")
    public void theCustomerCanSpendTheNewToken() {
        String token = null;
        try {
            token = adapter.customerApp.spendToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotNull(token);
    }

    @When("the manager requests a report of payments")
    public void theManagerRequestsAReportOfPayments() throws ReportFetchingException {
        report = adapter.managerApp.getPaymentsReport();
    }

    @Then("the report is generated")
    public void theReportIsGenerated() {
        Assert.assertNotNull(report);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        try {
            adapter.customerApp.retireAccount();
        } catch (Exception e) {
            System.out.println("Customer not registered");
        }
        try {
            adapter.merchantApp.retireAccount();
        } catch (Exception e) {
            System.out.println("Merchant not registered");
        }

        try {
            adapter.customerApp.retireBank();
        } catch (Exception e) {
            System.out.println("Customer bank not registered");
        }

        try {
            adapter.merchantApp.retireBank();
        } catch (Exception e) {
            System.out.println("Merchant bank not registered");
        }
    }
}

package api.merchant.impl;

import api.RestClient;
import api.merchant.lib.IMerchantApp;
import dtupay.dto.*;
import fastmoney.BankService;
import fastmoney.BankServiceException_Exception;
import fastmoney.BankServiceService;
import fastmoney.User;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class MerchantApp implements IMerchantApp {
    @Getter
    @Setter
    private Account account;
    private RestClient api;
    BankService bank = new BankServiceService().getBankServicePort();

    // Constructor
    public MerchantApp(Account account, RestClient api) {
        this.account = account;
        this.api = api;
    }

    @Override
    public void init(String firstName, String lastName, String identifier) {
        account = Account.builder()
                .firstName(firstName)
                .lastName(lastName)
                .identifier(identifier)
                .accountType(AccountType.MERCHANT)
                .build();
    }

    @Override
    public void registerBank(int balance) {
        System.out.println("Registering bank account");
        User user = new User();
        user.setFirstName(account.getFirstName());
        user.setLastName(account.getLastName());
        user.setCprNumber(account.getIdentifier());

        try {
            String bankId = bank.createAccountWithBalance(user, BigDecimal.valueOf(balance));
            System.out.println("Registered bank account with id: " + bankId);
            account.setBankId(bankId);
        } catch (BankServiceException_Exception e) {
            System.out.println("Error occurred during bank account registration: " + e);
        }
    }

    @Override
    public void retireBank() {
        System.out.println("Retiring bank account");
        System.out.println("Retiring bank account with id: " + account.getBankId());
        try {
            bank.retireAccount(account.getBankId());
            System.out.println("Retired bank account with id: " + account.getBankId());
        } catch (BankServiceException_Exception e) {
            System.out.println("Error occurred during bank account retirement: " + e);
        }
    }

    @Override
    public fastmoney.Account getBankAccount() {
        System.out.println("Getting bank account");
        try {
            return bank.getAccount(account.getBankId());
        } catch (BankServiceException_Exception e) {
            System.out.println("Error occurred during bank account retrieval: " + e);
        }
        return null;
    }

    @Override
    public void registerAccount() throws Exception {
        System.out.println("Registering DTUpay account");
        try {
            String id = api.post("/register", account, String.class);
            System.out.println("DTUpay id: " + id);
            account.setAccountId(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void retireAccount() throws Exception {
        System.out.println("Retiring DTUpay account");
        if (account == null) {
            throw new Exception("Merchant not registered");
        }
        String accountId = account.getAccountId();
        try {
            Response response = api.delete("/retire/" + accountId);
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                System.out.println("Merchant retirement successfully");
            } else {
                System.out.println("Merchant retirement failed with status: " + response.getStatus());
            }
        } catch (Exception e) {
            System.out.println("Error occurred during merchant retirement: " + e);
        }
    }

    @Override
    public void initiatePayment(Payment payment) throws Exception {
        System.out.println("Initiating payment " + payment.toString());
        String m = api.post("/pay", payment, String.class);
        System.out.println("response: " + m);
    }

}

package api.merchant.impl;

import api.RestClient;
import api.merchant.lib.IMerchantApp;
import dtupay.dto.*;
import exceptions.BankGetAccountException;
import exceptions.BankRetirementException;
import exceptions.DTUpayRegistrationException;
import exceptions.DTUpayRetirementException;
import fastmoney.BankService;
import fastmoney.BankServiceException_Exception;
import fastmoney.BankServiceService;
import fastmoney.User;
import lombok.Getter;
import lombok.Setter;
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
    public void retireBank() throws BankRetirementException {
        System.out.println("Retiring bank account with id: " + account.getBankId());
        try {
            bank.retireAccount(account.getBankId());
        } catch (BankServiceException_Exception e) {
            System.out.println("Error occurred during bank account retirement: " + e);
            throw new BankRetirementException(e.getMessage());
        }
    }

    @Override
    public fastmoney.Account getBankAccount() throws BankGetAccountException {
        System.out.println("Getting bank account");
        try {
            return bank.getAccount(account.getBankId());
        } catch (BankServiceException_Exception e) {
            System.out.println("Error occurred during bank account retirement: " + e);
            throw new BankGetAccountException(e.getMessage());
        }
    }

    public void registerAccount() throws DTUpayRegistrationException {
        System.out.println("Registering DTUpay account");
        try {
            String id = api.post("", account, String.class);
            System.out.println("DTUpay id: " + id);
            account.setAccountId(id);
        } catch (Exception e) {
            throw new DTUpayRegistrationException(e.getMessage());
        }
    }

    public void retireAccount() throws DTUpayRetirementException {
        System.out.println("Retiring DTUpay account");
        if (account == null) {
            throw new DTUpayRetirementException("Customer not registered");
        }
        String accountId = account.getAccountId();
        try {
            api.delete("/" + accountId);
        } catch (Exception e) {
            throw new DTUpayRetirementException(e.getMessage());
        }
    }

    @Override
    public void initiatePayment(Payment payment) throws Exception {
        System.out.println("Initiating payment " + payment.toString());
        String m = api.post("/payment", payment, String.class);
        System.out.println("response: " + m);
    }

}

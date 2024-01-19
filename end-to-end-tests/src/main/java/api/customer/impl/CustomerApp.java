package api.customer.impl;

import api.RestClient;
import api.customer.lib.ICustomerApp;

import dtupay.dto.Account;
import dtupay.dto.*;
import exceptions.*;
import fastmoney.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Stack;

@Data
public class CustomerApp implements ICustomerApp {
    private Account account;
    private Stack<String> tokens;
    private RestClient api;

    BankService bank = new BankServiceService().getBankServicePort();

    public CustomerApp(Account account, Stack<String> tokens, RestClient api) {
        this.account = account;
        this.tokens = tokens;
        this.api = api;
    }

    public void init(String firstName, String lastName, String identifier) {
        account = Account.builder()
                .firstName(firstName)
                .lastName(lastName)
                .identifier(identifier)
                .accountId(null)
                .accountType(AccountType.CUSTOMER)
                .build();
    }

    public void registerBank(int balance) throws BankRegistrationException {
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
            throw new BankRegistrationException(e.getMessage());
        }
    }

    public void retireBank() throws BankRetirementException {
        System.out.println("Retiring bank account with id: " + account.getBankId());
        try {
            bank.retireAccount(account.getBankId());
        } catch (BankServiceException_Exception e) {
            throw new BankRetirementException(e.getMessage());
        }
    }

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

    public String spendToken() throws TokenOperationException {
        System.out.println("Spending token");
        String t;
        try {
            t = tokens.pop();
            System.out.println("Spent token: " + t);
            System.out.println(t);
        } catch (Exception e) {
            throw new TokenOperationException("No tokens left");
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public void requestTokens() throws TokenFetchException {
        System.out.println("Requesting tokens");
        try {
            Stack<String> tks = (Stack<String>) api.get("/tokens/" + account.getBankId(), Stack.class);
            System.out.println("Received tokens: " + tks);
            tokens.addAll(tks);
            System.out.println("Tokens: " + tokens);

        } catch (Exception e) {
            throw new TokenFetchException(e.getMessage());
        }
    }

}

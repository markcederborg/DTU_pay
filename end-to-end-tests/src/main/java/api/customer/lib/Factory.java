package api.customer.lib;

import api.RestClient;
import api.customer.impl.CustomerApp;
import dtupay.dto.*;
import fastmoney.BankService;
import fastmoney.BankServiceService;

import java.util.Stack;

public class Factory {
    private static CustomerApp app = null;

    public synchronized CustomerApp getApp() {
        if (app != null) {
            return app;
        }

        // Assume these are default values or placeholders for Account and Tokens
        Account account = new Account();
        Stack<String> tokens = new Stack<String>(); // Tokens should be initialized properly
        BankService bank = new BankServiceService().getBankServicePort();
        RestClient api = new RestClient("http://localhost:8080/customer");

        app = new CustomerApp(account, tokens, api, bank);

        return app;
    }
}

package api.merchant.lib;

import dtupay.dto.*;
import exceptions.BankGetAccountException;
import exceptions.BankRetirementException;

public interface IMerchantApp {
    void init(String firstName, String lastName, String identifier);

    void retireBank() throws BankGetAccountException, BankRetirementException;

    fastmoney.Account getBankAccount() throws BankGetAccountException;

    void registerAccount() throws Exception;

    void registerBank(int balance);

    void retireAccount() throws Exception;

    void initiatePayment(Payment payment) throws Exception;

}

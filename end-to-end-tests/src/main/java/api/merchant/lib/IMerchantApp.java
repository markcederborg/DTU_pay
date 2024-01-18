package api.merchant.lib;

import dtupay.dto.*;

public interface IMerchantApp {
    void init(String firstName, String lastName, String identifier);

    void retireBank();

    fastmoney.Account getBankAccount();

    void registerAccount() throws Exception;

    void registerBank(int balance);

    void retireAccount() throws Exception;

    void initiatePayment(Payment payment) throws Exception;

}

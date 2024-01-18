package utils;

import api.customer.impl.CustomerApp;
import api.manager.impl.ManagerApp;
import api.merchant.impl.MerchantApp;

public class Adapter {
    public CustomerApp customerApp;
    public MerchantApp merchantApp;
    public ManagerApp managerApp;
    public Adapter(CustomerApp customerApp, MerchantApp merchantApp, ManagerApp managerApp) {
        this.customerApp = customerApp;
        this.merchantApp = merchantApp;
        this.managerApp = managerApp;
    }
}

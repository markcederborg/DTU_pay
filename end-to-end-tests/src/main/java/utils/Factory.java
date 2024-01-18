package utils;

import api.customer.impl.CustomerApp;
import api.manager.impl.ManagerApp;
import api.merchant.impl.MerchantApp;

public class Factory {
    static Adapter adapter = null;

    public synchronized Adapter getAdapter() {
        if (adapter != null) {
            return adapter;
        }

        CustomerApp customerApp = new api.customer.lib.Factory().getApp();
        MerchantApp merchantApp = new api.merchant.lib.Factory().getApp();
        ManagerApp managerApp = new api.manager.lib.Factory().getApp();

        adapter = new Adapter(customerApp, merchantApp, managerApp);

        return  adapter;
    }
}

package api.merchant.lib;

import api.RestClient;
import api.merchant.impl.MerchantApp;
import dtupay.dto.*;

public class Factory {
	static MerchantApp app = null;

	public synchronized MerchantApp getApp() {
		if (app != null) {
			return app;
		}
		Account account = new Account().builder().accountId(null).accountType(AccountType.MERCHANT).build();
		RestClient api = new RestClient("http://localhost:8082/merchant");
		//BankService bank = new BankServiceService().getBankServicePort();
		app = new MerchantApp(account, api);

		return app;
	}
}

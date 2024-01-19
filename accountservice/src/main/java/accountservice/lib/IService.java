package accountservice.lib;

import messaging.Event;

public interface IService {

    void handleCustomerRetirementRequested(Event event);

    void handleMerchantRetirementRequested(Event event);

    void handleAllAccountsRequested(Event event);

}

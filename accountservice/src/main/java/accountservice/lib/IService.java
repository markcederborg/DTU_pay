package accountservice.lib;

import dtupay.dto.Author;
import messaging.Event;

@Author // S220042
public interface IService {

    void handleCustomerRetirementRequested(Event event);

    void handleMerchantRetirementRequested(Event event);

    void handleAllAccountsRequested(Event event);

}

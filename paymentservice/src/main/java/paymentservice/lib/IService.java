package paymentservice.lib;

import messaging.Event;

public interface IService {
    void handlePaymentRequested(Event ev);

}

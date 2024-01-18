package idgeneratorservice.lib;

import messaging.Event;

public interface IService {
    void handlePaymentIdRequested(Event ev);

    void handleIdRequested(Event ev);

    void handleHashRequested(Event ev);
}

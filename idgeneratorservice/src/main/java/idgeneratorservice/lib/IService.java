package idgeneratorservice.lib;

import dtupay.dto.Author;
import messaging.Event;

@Author // s2200442
public interface IService {
    void handlePaymentIdRequested(Event ev);

    void handleIdRequested(Event ev);

    void handleHashRequested(Event ev);

}

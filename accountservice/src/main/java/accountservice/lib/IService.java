package accountservice.lib;

import messaging.Event;

public interface IService {


    void handleRetirementRequested(Event event) ;

    void handleAllAccountsRequested(Event event);
}

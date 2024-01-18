package idgeneratorservice.lib;

import idgeneratorservice.impl.IdGenerator;
import idgeneratorservice.impl.Service;
import messaging.implementations.RabbitMqQueue;

public class Factory {


    static IService service = null;


    public synchronized IService getService() {
        // The singleton pattern.
        // Ensure that there is at most
        // one instance of a PaymentService
        if (service != null) {
            return service;
        }

        // Hookup the classes to send and receive
        // messages via RabbitMq, i.e. RabbitMqSender and
        // RabbitMqListener.
        // This should be done in the factory to avoid
        // the PaymentService knowing about them. This
        // is called dependency injection.
        // At the end, we can use the PaymentService in tests
        // without sending actual messages to RabbitMq.
        var mq = new RabbitMqQueue("rabbitmq");
        IdGenerator idGenerator = new IdGenerator();
        service = new Service(mq, idGenerator);
        // new Adapter(service, mq);
        return service;
    }
}

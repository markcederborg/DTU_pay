package dtupay.service.lib;

import dtupay.service.impl.Service;
import messaging.implementations.RabbitMqQueue;


public class Factory {
	static Service service = null;

	public synchronized Service getService() {
		if (service != null) {
			return service;
		}


		// Create the RabbitMQ queue instance
		var mq = new RabbitMqQueue("rabbitmq");

		// Inject the Logger and RabbitMQ queue into the Service
		service = new Service(mq);

		return service;
	}
}

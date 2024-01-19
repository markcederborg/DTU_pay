package service.lib;

import messaging.implementations.RabbitMqQueue;
import service.impl.Service;


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

package idgeneratorservice.impl;

import dtupay.dto.*;

import idgeneratorservice.lib.IService;
import messaging.Event;
import messaging.MessageQueue;

public class Service implements IService {
	private final MessageQueue queue;
	private final IdGenerator idGenerator;


	public Service(MessageQueue q, IdGenerator idGenerator) {
		this.queue = q;
		this.idGenerator = idGenerator;
		this.queue.addHandler("id.requested", this::handleIdRequested);
		this.queue.addHandler("hash.requested", this::handleHashRequested);
		this.queue.addHandler("payment.id.requested", this::handlePaymentIdRequested);
	}

	@Override
	public void handlePaymentIdRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var payment = ev.getArgument(1, Payment.class);
		try {
			// Generate a unique account ID
			String generatedId = idGenerator.generateHash(payment.getMerchantId());
			ev = new Event("payment.id.succeeded", new Object[] { correlationId, generatedId });
		} catch (Exception e) {
			ev = new Event("payment.id.failed", new Object[] { correlationId, e });
		}
		// Publish the event
		queue.publish(ev);
	}

	@Override
	public void handleIdRequested(Event ev) {
		System.out.println("Received id request");
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			// Generate a unique account ID
			String generatedId = idGenerator.generateId();

			ev = new Event("id.succeeded", new Object[] { correlationId, generatedId });
		} catch (Exception e) {
			ev = new Event("id.failed", new Object[] { correlationId, e });
		}
		System.out.println("Sending id response");
		// Publish the event
		queue.publish(ev);
	}

	@Override
	public void handleHashRequested(Event ev) {
		var account = ev.getArgument(1, Account.class);
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			// Generate a unique account ID
			String generatedId = idGenerator.generateHash(account.getBankId());
			account.setAccountId(generatedId);
			ev = new Event("id.succeeded", new Object[] { correlationId, account });
		} catch (Exception e) {
			ev = new Event("id.failed", new Object[] { correlationId, e });
		}
		// Publish the event
		queue.publish(ev);
	}


}

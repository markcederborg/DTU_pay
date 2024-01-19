package service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import dtupay.dto.*;
import service.lib.IService;
import messaging.Event;
import messaging.MessageQueue;

public class Service implements IService {
	private MessageQueue queue;
	private Map<CorrelationId, CompletableFuture<String>> registrations = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<Boolean>> retirements = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<Boolean>> payments = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<List<Account>>> accounts = new ConcurrentHashMap<>();

	public Service(MessageQueue q) {
		queue = q;
		queue.addHandler("merchant.registration.succeeded", this::handleRegistrationSucceed);
		queue.addHandler("merchant.retirement.succeeded", this::handleRetirementSucceeded);
		queue.addHandler("payment.succeeded", this::handlePaymentSucceeded);
		queue.addHandler("merchant.registration.failed", this::handleAccountRegistrationFailed);
		queue.addHandler("merchant.retirement.failed", this::handleAccountDeregistrationFailed);

	}

	/*
	 * Outgoing events:
	 */

	public CompletableFuture<List<Account>> getAccounts() {
		var correlationId = CorrelationId.randomId();
		accounts.put(correlationId, new CompletableFuture<>());
		Event event = new Event("merchant.accounts.requested", new Object[] { correlationId });
		queue.publish(event);
		return accounts.get(correlationId);
	}

	public CompletableFuture<String> registerAccount(Account account) {
		var correlationId = CorrelationId.randomId();
		registrations.put(correlationId, new CompletableFuture<>());
		Event event = new Event("registration.requested", new Object[] { correlationId, account });
		queue.publish(event);
		return registrations.get(correlationId);
	}

	public CompletableFuture<Boolean> retireAccount(String id) {
		var correlationId = CorrelationId.randomId();
		retirements.put(correlationId, new CompletableFuture<>());
		Event event = new Event("merchant.retirement.requested", new Object[] { correlationId, id });
		queue.publish(event);
		return retirements.get(correlationId);
	}

	public CompletableFuture<Boolean> pay(Payment payment) {
		var correlationId = CorrelationId.randomId();
		payments.put(correlationId, new CompletableFuture<>());
		Event event = new Event("payment.requested", new Object[] { correlationId, payment });
		queue.publish(event);
		return payments.get(correlationId);
	}

	/*
	 * Listeners:
	 */
	private void handleRetirementSucceeded(Event e) {
		var future = retirements.get(e.getArgument(0, CorrelationId.class));
		future.complete(true);
	}

	private void handlePaymentSucceeded(Event e) {
		var future = payments.get(e.getArgument(0, CorrelationId.class));
		future.complete(true);
	}

	private void handleRegistrationSucceed(Event e) {
		var future = registrations.get(e.getArgument(0, CorrelationId.class));
		future.complete(e.getArgument(1, String.class));
	}

	private void handleAccountDeregistrationFailed(Event e) {
		var future = retirements.get(e.getArgument(0, CorrelationId.class));
		var s = e.getArgument(1, Exception.class);
		future.completeExceptionally(s);
	}

	private void handleAccountRegistrationFailed(Event e) {
		var future = registrations.get(e.getArgument(0, CorrelationId.class));
		var s = e.getArgument(1, Exception.class);
		future.completeExceptionally(s);
	}
}

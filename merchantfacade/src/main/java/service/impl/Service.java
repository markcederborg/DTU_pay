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

		queue.addHandler("merchant.retirement.succeeded", this::handleRetirementSucceeded);
		queue.addHandler("merchant.registration.succeeded", this::handleRegistrationSucceed);
		queue.addHandler("payment.succeeded", this::handlePaymentSucceeded);
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
		Event event = new Event("retirement.requested", new Object[] { correlationId, id, });
		queue.publish(event);
		return retirements.get(correlationId);
	}

	public CompletableFuture<Boolean> pay(Payment payment) {
		var correlationId = CorrelationId.randomId();
		payments.put(correlationId, new CompletableFuture<>()); // Ensure this is the same map used in the handlePaymentSucceeded method
		Event event = new Event("payment.requested", new Object[] { correlationId, payment });
		queue.publish(event);
		return payments.get(correlationId); // This will return immediately, but the CompletableFuture will be completed later
	}


	/*
	 * Listeners:
	 */

	private void handleRetirementSucceeded(Event e) {
		var future = retirements.get(e.getArgument(0, CorrelationId.class));
		if (future != null) {
			try {
				future.complete(true);
			} catch (ClassCastException ex) {
				future.completeExceptionally(new Exception("Retirement failed..."));
			}
		}
	}

	private void handlePaymentSucceeded(Event e) {
		System.out.println("Payment succeeded");
		var correlationId = e.getArgument(0, CorrelationId.class);
		CompletableFuture<Boolean> future = payments.get(correlationId);
		if (future != null) {
			future.complete(true);
		} else {
			System.err.println("No CompletableFuture found for correlation ID: " + correlationId);
		}
	}


	private void handleRegistrationSucceed(Event e) {
		var future = registrations.get(e.getArgument(0, CorrelationId.class));
		if (future != null) {
			try {
				String id = e.getArgument(1, String.class);
				System.out.println("Received ID: " + id);
				future.complete(id);
			} catch (ClassCastException ex) {
				future.completeExceptionally(new Exception("Invalid account data received."));
			}
		}
	}
}

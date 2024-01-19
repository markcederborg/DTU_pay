package service.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import messaging.Event;
import messaging.MessageQueue;
import service.lib.IService;
import dtupay.dto.*;
import java.util.Stack;

public class Service implements IService {
	private MessageQueue queue;
	private Map<CorrelationId, CompletableFuture<String>> registrations = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<Boolean>> retirements = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<Stack<String>>> tokens = new ConcurrentHashMap<>();

	public Service(MessageQueue q) {
		queue = q;
		queue.addHandler("customer.registration.succeeded", this::handleAccountRegistrationSucceeded);
		queue.addHandler("customer.registration.failed", this::handleAccountRegistrationFailed);
		queue.addHandler("customer.retirement.succeeded", this::handleAccountDeregistrationSucceeded);
		queue.addHandler("customer.retirement.failed", this::handleAccountDeregistrationFailed);
		queue.addHandler("tokens.succeeded", this::handleTokensRequestSucceeded);
		queue.addHandler("tokens.failed", this::handleTokensRequestFailed);
	}

	/*
	 * Outgoing events:
	 */
	public CompletableFuture<String> register(Account account) {
		var correlationId = CorrelationId.randomId();
		registrations.put(correlationId, new CompletableFuture<>());
		Event event = new Event("registration.requested", new Object[] { correlationId, account });
		queue.publish(event);
		return registrations.get(correlationId);
	}

	public CompletableFuture<Boolean> retireAccount(String id) {
		var correlationId = CorrelationId.randomId();
		retirements.put(correlationId, new CompletableFuture<>());
		Event event = new Event("customer.retirement.requested", new Object[] { correlationId, id, });
		queue.publish(event);
		return retirements.get(correlationId);
	}

	public CompletableFuture<Stack<String>> getTokens(String id) {
		var correlationId = CorrelationId.randomId();
		tokens.put(correlationId, new CompletableFuture<>());
		Event event = new Event("tokens.requested", new Object[] { correlationId, id });
		queue.publish(event);
		return tokens.get(correlationId);
	}

	/*
	 * Listeners:
	 */

	private void handleAccountRegistrationSucceeded(Event e) {
		var future = registrations.get(e.getArgument(0, CorrelationId.class));
		var s = e.getArgument(1, String.class);
		future.complete(s);
	}

	private void handleAccountRegistrationFailed(Event e) {
		var future = registrations.get(e.getArgument(0, CorrelationId.class));
		var s = e.getArgument(1, Exception.class);
		future.completeExceptionally(s);
	}

	private void handleAccountDeregistrationSucceeded(Event e) {
		var correlationId = e.getArgument(0, CorrelationId.class);
		var future = retirements.get(correlationId);
		future.complete(true);
	}

	private void handleAccountDeregistrationFailed(Event e) {
		var future = retirements.get(e.getArgument(0, CorrelationId.class));
		var s = e.getArgument(1, Exception.class);
		future.completeExceptionally(s);
	}

	private void handleTokensRequestSucceeded(Event e) {
		var future = tokens.get(e.getArgument(0, CorrelationId.class));
		Stack<String> ts = (Stack<String>) e.getArgument(1, Stack.class);
		future.complete(ts);
	}

	private void handleTokensRequestFailed(Event e) {
		var future = tokens.get(e.getArgument(0, CorrelationId.class));
		var s = e.getArgument(1, Exception.class);
		future.completeExceptionally(s);
	}
}

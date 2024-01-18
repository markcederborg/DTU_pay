package service.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import service.lib.IService;
import messaging.Event;
import messaging.MessageQueue;
import dtupay.dto.Account;
import dtupay.dto.CorrelationId;
import dtupay.dto.Payment;

import java.util.*;

public class Service implements IService {
	private MessageQueue queue;
	private Map<CorrelationId, CompletableFuture<List<Account>>> accounts = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<Boolean>> deletes = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<ArrayList<Payment>>> reports = new ConcurrentHashMap<>();

	public Service(MessageQueue q) {
		queue = q;
		queue.addHandler("all.accounts.succeeded", this::handleAccountsSucceeded);
		queue.addHandler("all.accounts.failed", this::handleAccountsFailed);
		queue.addHandler("accounts.delete.succeeded", this::handleAccountsDeleteSucceeded);
		queue.addHandler("accounts.delete.failed", this::handleAccountsDeleteFailed);
		queue.addHandler("payments.report.succeeded", this::handlePaymentsReportSucceeded);
	}

	/*
	 * Outgoing events:
	 */
	public CompletableFuture<List<Account>> getAccounts() {
		var correlationId = CorrelationId.randomId();
		accounts.put(correlationId, new CompletableFuture<>());
		Event event = new Event("all.accounts.requested", new Object[] { correlationId });
		queue.publish(event);
		return accounts.get(correlationId);
	}

	public CompletableFuture<Boolean> deleteAccounts() {
		var correlationId = CorrelationId.randomId();
		deletes.put(correlationId, new CompletableFuture<>());
		Event event = new Event("accounts.delete.requested", new Object[] { correlationId });
		queue.publish(event);
		return deletes.get(correlationId);
	}

	@Override
	public CompletableFuture<ArrayList<Payment>> getPayments() {
		var correlationId = CorrelationId.randomId();
		reports.put(correlationId, new CompletableFuture<>());
		Event event = new Event("payments.report.requested", new Object[] { correlationId });
		queue.publish(event);
		return reports.get(correlationId);
	}

	/*
	 * Listeners:
	 */
	private void handleAccountsFailed(Event event) {
		CorrelationId correlationId = event.getArgument(0, CorrelationId.class);
		var future = accounts.get(correlationId);
		if (future != null) {
			try {
				String errorMessage = event.getArgument(1, String.class);
				future.completeExceptionally(new Exception(errorMessage));
			} catch (ClassCastException ex) {
				future.completeExceptionally(new Exception("Invalid account data received."));
			}
		}

	}

	private void handleAccountsDeleteFailed(Event event) {
		CorrelationId correlationId = event.getArgument(0, CorrelationId.class);
		CompletableFuture<Boolean> future = deletes.get(correlationId);
		if (future != null) {
			try {
				String errorMessage = event.getArgument(1, String.class);
				future.completeExceptionally(new Exception(errorMessage));
			} catch (ClassCastException ex) {
				future.completeExceptionally(new Exception("Invalid account data received."));
			}
		}
	}

	private void handleAccountsDeleteSucceeded(Event event) {
		CorrelationId correlationId = event.getArgument(0, CorrelationId.class);
		CompletableFuture<Boolean> future = deletes.get(correlationId);

		if (future != null) {
			try {
				Boolean success = event.getArgument(1, Boolean.class); // Cast to the appropriate type
				future.complete(success);
			} catch (ClassCastException ex) {
				future.completeExceptionally(new Exception("Invalid account data received."));
			}
		}
	}

	private void handleAccountsSucceeded(Event e) {
		var s = e.getArgument(1, List.class);
		var correlationid = e.getArgument(0, CorrelationId.class);
		accounts.get(correlationid).complete(s);
	}

	private void handlePaymentsReportSucceeded(Event e) {
		var correlationid = e.getArgument(0, CorrelationId.class);
		var s = e.getArgument(1, ArrayList.class);
		System.out.println("Received report: " + s);
		reports.get(correlationid).complete(s);
	}
}
package accountservice.impl;

import dtupay.dto.*;
import accountservice.lib.IRepository;
import accountservice.lib.IService;
import messaging.Event;
import messaging.MessageQueue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
	private final IRepository repository;
	private final MessageQueue queue;

	private Map<CorrelationId, CompletableFuture<String>> ids = new ConcurrentHashMap<>();

	public Service(MessageQueue q, IRepository repo) {
		this.queue = q;
		this.repository = repo;
		this.queue.addHandler("registration.requested", this::handleRegistrationRequested);
		this.queue.addHandler("merchant.retirement.requested", this::handleMerchantRetirementRequested);
		this.queue.addHandler("customer.retirement.requested", this::handleCustomerRetirementRequested);
		this.queue.addHandler("all.accounts.requested", this::handleAllAccountsRequested);
		this.queue.addHandler("id.succeeded", this::handleIdSucceeded);

	}

	public void handleAllAccountsRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			var accounts = repository.getAccounts();
			ev = new Event("all.accounts.succeeded", new Object[] { correlationId, accounts });
		} catch (Exception e) {
			ev = new Event("all.accounts.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	public void handleRegistrationRequested(Event ev) {
		System.out.println("Received registration request");
		Account account = ev.getArgument(1, Account.class);
		String dest = (account.getAccountType() == AccountType.MERCHANT) ? "merchant" : "customer";
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			System.out.println("Received registration request for: " + account);
			ids.put(correlationId, new CompletableFuture<>());
			Event event = new Event("id.requested", new Object[] { correlationId, account });
			queue.publish(event);

			// Generate a unique account ID
			String generatedId = ids.get(correlationId).join();
			System.out.println("Generated ID: " + generatedId);
			account.setAccountId(generatedId);

			// Add the account to the repository
			repository.addAccount(account);

			ev = new Event(dest + ".registration.succeeded", new Object[] { correlationId, generatedId });
		} catch (Exception e) {
			ev = new Event(dest + ".registration.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	public void handleCustomerRetirementRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var id = ev.getArgument(1, String.class);
		try {
			repository.removeAccount(id);
			ev = new Event("customer.retirement.succeeded", new Object[] {
					correlationId, id });
		} catch (Exception e) {
			ev = new Event("customer.retirement.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	public void handleMerchantRetirementRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var id = ev.getArgument(1, String.class);
		try {
			repository.removeAccount(id);
			ev = new Event("merchant.retirement.succeeded", new Object[] {
					correlationId, id });
		} catch (Exception e) {
			ev = new Event("merchant.retirement.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	private void handleIdSucceeded(Event ev) {
		System.out.println("Received id response");
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var generatedId = ev.getArgument(1, String.class);
		CompletableFuture<String> idFuture = ids.get(correlationId);
		if (idFuture != null) {
			idFuture.complete(generatedId);
		} else {
			System.out.println("No future found for correlation ID: " + correlationId);
		}
	}
}

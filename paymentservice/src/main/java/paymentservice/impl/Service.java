package paymentservice.impl;

import fastmoney.BankService;
import fastmoney.BankServiceException_Exception;
import fastmoney.BankServiceService;

import paymentservice.lib.IRepository;
import paymentservice.lib.IService;
import messaging.Event;
import messaging.MessageQueue;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import dtupay.dto.CorrelationId;
import dtupay.dto.Payment;

public class Service implements IService {
	private final IRepository repository;
	private final MessageQueue queue;

	BankService bank = new BankServiceService().getBankServicePort();

	private Map<CorrelationId, CompletableFuture<String>> customerIds = new ConcurrentHashMap<>();
	private Map<CorrelationId, CompletableFuture<String>> paymentIds = new ConcurrentHashMap<>();

	public Service(MessageQueue q, IRepository repo) {
		this.queue = q;
		this.repository = repo;
		this.queue.addHandler("payment.requested", this::handlePaymentRequested);
		this.queue.addHandler("token.id.succeeded", this::handleTokenIdSucceeded);
		this.queue.addHandler("payment.id.succeeded", this::handlePaymentIdSucceeded);
	}

	public void handlePaymentRequested(Event ev) {
		System.out.println("handling payment request");
		Payment payment = ev.getArgument(1, Payment.class);
		System.out.println("payment: " + payment);
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			String token = payment.getToken();

			if (token == null) {
				throw new Exception("Token is null");
			}
			System.out.println("token id request");
			customerIds.put(correlationId, new CompletableFuture<>());
			Event ev2 = new Event("token.id.requested", new Object[] { correlationId, token });
			queue.publish(ev2);
			// wait for response

			String customerId = customerIds.get(correlationId).get();
			System.out.println("customerId: " + customerId);

			paymentIds.put(correlationId, new CompletableFuture<>());

			Event ev3 = new Event("payment.id.requested", new Object[] { correlationId, payment });
			queue.publish(ev3);
			// wait for response
			String paymentId = paymentIds.get(correlationId).get();
			payment.setId(paymentId);

			try {
				bank.transferMoneyFromTo(customerId, payment.getMerchantId(), BigDecimal.valueOf(payment.getAmount()),
						payment.getDescription());
			} catch (BankServiceException_Exception e) {
				throw new Exception("BankServiceException_Exception: " + e.getMessage());
			}
			Event useTokenEvent = new Event("token.used", new Object[] { correlationId, token });
			queue.publish(useTokenEvent);

			Event storePayment = new Event("payment.storage.requested", new Object[] { correlationId, payment });
			queue.publish(storePayment);

			ev = new Event("payment.succeeded", new Object[] { correlationId });
		} catch (Exception e) {
			ev = new Event("payment.failed", new Object[] { correlationId, e });
		}
		System.out.println("publishing payment event");
		queue.publish(ev);
	}

	public void handleTokenIdSucceeded(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		String customerId = ev.getArgument(1, String.class);
		customerIds.get(correlationId).complete(customerId);
	}

	public void handlePaymentIdSucceeded(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		var paymentId = ev.getArgument(1, String.class);
		paymentIds.get(correlationId).complete(paymentId);
	}

}

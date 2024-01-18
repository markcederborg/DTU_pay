package reportservice.impl;

import reportservice.lib.IRepository;
import reportservice.lib.IService;
import messaging.Event;
import messaging.MessageQueue;

import dtupay.dto.*;

import java.util.List;

public class Service implements IService {
	private final IRepository repository;
	private final MessageQueue queue;

	public Service(MessageQueue q, IRepository repo) {
		this.queue = q;
		this.repository = repo;
		this.queue.addHandler("customer.report.requested", this::handleCustomerReportRequested);
		this.queue.addHandler("merchant.report.requested", this::handleMerchantReportRequested);
		this.queue.addHandler("manager.report.requested", this::handleManagerReportRequested);
		this.queue.addHandler("payment.storage.requested", this::handlePaymentReceived);
		this.queue.addHandler("payments.report.requested", this::handlePaymentsReportRequested);
	}

	@Override
	public void handlePaymentReceived(Event ev) {
		var payment = ev.getArgument(1, Payment.class);
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			repository.addTransaction(payment);
			ev = new Event("payment.storage.succeeded", new Object[] { correlationId });
		} catch (Exception e) {
			ev = new Event("payment.storage.failed", new Object[] { correlationId, e });
		}

		// Publish the event
		queue.publish(ev);
	}

	private void handlePaymentsReportRequested(Event ev) {
		var correlationId = ev.getArgument(0, CorrelationId.class);
		try {
			List<Payment> transactions = repository.getTransactions();
			ev = new Event("payments.report.succeeded", new Object[] { correlationId, transactions });
		} catch (Exception e) {
			ev = new Event("payments.report.failed", new Object[] { correlationId, e });
		}
		queue.publish(ev);
	}

	@Override
	public void handleMerchantReportRequested(Event ev) {

	}

	@Override
	public void handleManagerReportRequested(Event ev) {

	}

	@Override
	public void handleCustomerReportRequested(Event ev) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'handleCustomerReportRequested'");
	}
}

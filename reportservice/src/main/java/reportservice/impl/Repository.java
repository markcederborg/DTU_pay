package reportservice.impl;

import dtupay.dto.Payment;
import reportservice.lib.IRepository;

import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private ArrayList<Payment> transactions = new ArrayList<Payment>();

    public void addTransaction(Payment payment) {
        transactions.add(payment);
    }

    public List<Payment> getTransactions() {
        return transactions;
    }
}

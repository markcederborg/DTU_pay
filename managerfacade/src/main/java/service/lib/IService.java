package service.lib;

import java.util.concurrent.CompletableFuture;

import dtupay.dto.Payment;
import java.util.*;

public interface IService {

    CompletableFuture<ArrayList<Payment>> getPayments();
}

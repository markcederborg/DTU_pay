package dtupay.service.lib;

import java.util.concurrent.CompletableFuture;
import dtupay.dto.Account;

public interface IService {
    public CompletableFuture<String> register(Account account) throws Exception;

    public CompletableFuture<Boolean> retireAccount(String id) throws Exception;

 
}

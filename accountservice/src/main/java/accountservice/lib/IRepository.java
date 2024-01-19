package accountservice.lib;

import java.util.List;

import dtupay.dto.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.ws.rs.NotFoundException;

public interface IRepository {
    void addAccount(Account account) throws Exception;

    Account getAccount(String id) throws NotFoundException, AccountNotFoundException;

    void removeAccount(String id) throws NotFoundException, Exception;

    List<Account> getAccounts() throws Exception;

}

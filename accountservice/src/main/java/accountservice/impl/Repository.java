package accountservice.impl;

import accountservice.dto.Account;
import accountservice.lib.IRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.AccountNotFoundException;
import javax.ws.rs.NotFoundException;

public class Repository implements IRepository {

    @Getter
    @Setter
    private List<Account> accounts = new ArrayList<Account>();

    @Override
    public void addAccount(Account account) throws Exception {
        accounts.add(account);
    }

    @Override
    public Account getAccount(String id) throws NotFoundException, AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountId().equals(id)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Account with id: " + id + " not found");
    }

    @Override
    public void removeAccount(String id) throws NotFoundException, Exception {
        Account account = getAccount(id);
        accounts.remove(account);
    }
}

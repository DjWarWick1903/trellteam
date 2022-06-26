package ro.dev.trellteam;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.dev.trellteam.model.Account;
import ro.dev.trellteam.model.Role;
import ro.dev.trellteam.service.AccountService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountUnitTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void findAllTest() {
        List<Account> accounts = accountService.list();

        Assert.assertEquals(accounts.size(), 5);
    }

    @Test
    public void findByUsernameTest() {
        Account account = accountService.getAccount("robertpop");

        Assert.assertEquals(account.getRoles().size(), 1);
    }

    @Test
    public void assignRoleTest() {
        Role role = new Role(2l, "ADMIN");
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);

        Account account = accountService.getAccount("robertpop");
        account.setRoles(roles);
        accountService.save(account, false);
    }
}

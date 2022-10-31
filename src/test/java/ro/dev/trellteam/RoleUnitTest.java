package ro.dev.trellteam;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.dev.trellteam.domain.Role;
import ro.dev.trellteam.web.service.RoleService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleUnitTest {

    @Autowired
    RoleService roleService;

    @Test
    public void findAllTest() {
        List<Role> roles = roleService.list();

        Assert.assertEquals(roles.size(), 1);
    }

    @Test
    public void findByName() {
        Role role = roleService.findByName("ADMIN");

        Assert.assertEquals(role.getId(), Long.valueOf(1));
    }

    @Test
    public void deleteTest() {
        roleService.deleteRoleByName("ADMIN");

        Role role = roleService.findByName("ADMIN");

        Assert.assertEquals(role, null);
    }

    @Test
    public void saveTest() {
        Role role = new Role(null, "ADMIN");
        role = roleService.save(role);

        Assert.assertEquals(role.getName(), "ADMIN");
    }
}

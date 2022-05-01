package ro.dev.trellteam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.dev.trellteam.model.Role;
import ro.dev.trellteam.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Method used to return all roles.
     * @return List
     */
    public List<Role> list() {
        log.info("RoleService--Fetching all roles...");
        return roleRepository.findAll();
    }

    /**
     * Method used to return a role starting from it's name.
     * @param name
     * @return Role
     */
    public Role findByName(final String name) {
        log.info("RoleService--Fetching role {}" , name);
        return roleRepository.findByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Role findById(final Long id) {
        return roleRepository.findById(id).get();
    }

    /**
     * Method used to save a role into the database.
     * @param role
     * @return Role
     */
    public Role save(Role role) {
        log.info("RoleService--Adding role {} to the database" , role.getName());
        return roleRepository.save(role);
    }

    /**
     * Method used to delete a role starting from it's name.
     * @param name
     */
    public void deleteRoleByName(String name) {
        log.info("RoleService--Deleting role {} from the database" , name);
        roleRepository.deleteByName(name);
    }
}

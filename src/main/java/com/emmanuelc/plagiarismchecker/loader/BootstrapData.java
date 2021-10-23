package com.emmanuelc.plagiarismchecker.loader;

import com.emmanuelc.plagiarismchecker.domain.models.Privilege;
import com.emmanuelc.plagiarismchecker.domain.models.Role;
import com.emmanuelc.plagiarismchecker.domain.models.User;
import com.emmanuelc.plagiarismchecker.domain.models.enumerations.PrivilegeEnum;
import com.emmanuelc.plagiarismchecker.domain.models.enumerations.RoleEnum;
import com.emmanuelc.plagiarismchecker.repository.PrivilegeRepo;
import com.emmanuelc.plagiarismchecker.repository.RoleRepo;
import com.emmanuelc.plagiarismchecker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BootstrapData {
    boolean alreadySetup = false;

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final PrivilegeRepo privilegeRepo;

    private final PasswordEncoder encoder;

    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createAllRolesAndPrivileges();

        Role adminRole = roleRepo.findByName(RoleEnum.ADMIN);
        User user = new User()
                .setEmail("admin@localhost.com")
                .setPassword(encoder.encode("admin"))
                .setFirstname("Admin")
                .setLastname("Admin")
                .addRole(adminRole);
        if (userRepo.findByEmail(user.getEmail()).isEmpty()) {
            userRepo.save(user);
        }

        alreadySetup = true;
    }

    @Transactional
    void createAllRolesAndPrivileges() {
        Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeEnum.READ);
        Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeEnum.WRITE);
        Privilege deletePrivilege = createPrivilegeIfNotFound(PrivilegeEnum.DELETE);

        Set<Privilege> adminPrivileges = Set.of(readPrivilege, writePrivilege, deletePrivilege);
        Set<Privilege> staffPrivileges = Set.of(readPrivilege, writePrivilege);
        Set<Privilege> userPrivileges = Set.of(readPrivilege);

        createRoleIfNotFound(RoleEnum.ADMIN, adminPrivileges);
        createRoleIfNotFound(RoleEnum.STAFF, staffPrivileges);
        createRoleIfNotFound(RoleEnum.USER, userPrivileges);
    }

    @Transactional
    void createRoleIfNotFound(RoleEnum name, Set<Privilege> privileges) {
        Role role = roleRepo.findByName(name);
        if (role == null) {
            role = new Role()
                    .setName(name)
                    .setPrivileges(privileges);
            roleRepo.save(role);
        }
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(PrivilegeEnum name) {
        Privilege privilege = privilegeRepo.findByName(name);
        if (privilege == null) {
            privilege = new Privilege().setName(name);
            privilegeRepo.save(privilege);
        }
        return privilege;
    }
}

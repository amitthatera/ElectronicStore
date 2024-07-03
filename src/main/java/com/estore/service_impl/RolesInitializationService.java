package com.estore.service_impl;

import com.estore.entities.Roles;
import com.estore.repository.RolesRepository;
import com.estore.utility.AppConstant;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesInitializationService {

    private final RolesRepository roleRepo;

    public RolesInitializationService(RolesRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Transactional
    public void initializeRoles() {
        createRoleIfNotFound(AppConstant.ADMIN, "ROLE_ADMIN");
        createRoleIfNotFound(AppConstant.NORMAL, "ROLE_NORMAL");
    }

    private void createRoleIfNotFound(Long roleId, String roleName) {
        Optional<Roles> roleOpt = roleRepo.findById(roleId);
        if (roleOpt.isEmpty()) {
            Roles role = Roles.builder()
                    .roleId(roleId)
                    .roleName(roleName)
                    .build();
            roleRepo.save(role);
        }
    }
}

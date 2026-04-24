package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.Role;
import com.aigovernance.lifecycle.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepo;

    public List<Role> listAll() { return roleRepo.findAllRolesQuery(); }

    public void insertRole(Role role) {
        if (roleRepo.existsById(role.getRoleId())) throw new RuntimeException("Duplicate Role ID");
        roleRepo.insertRoleQuery(role.getRoleId(), role.getRoleName(), role.getRoleDescription());
    }

    public List<Map<String, Object>> getUsersRoles() { return roleRepo.findUsersAndRoles(); }
    public List<Map<String, Object>> getActivePermissions() { return roleRepo.findActiveRolePermissions(); }
    public List<Map<String, Object>> getRoleStats() { return roleRepo.findRoleCountsPerDataset(); }
    public List<Map<String, Object>> getPrivilegedUsers() { return roleRepo.findPrivilegedUsers(); }
    public List<Map<String, Object>> getUserDatasetAccessReport() {
        return roleRepo.findUserDatasetAccess();
    }
    public void updateRole(Integer id, String name, String desc) { roleRepo.updateRoleQuery(id, name, desc); }
    public void cleanupRoles() { roleRepo.deleteUnusedRolesQuery(); }
    public void removeRole(String name) { roleRepo.deleteRoleByNameQuery(name); }
}
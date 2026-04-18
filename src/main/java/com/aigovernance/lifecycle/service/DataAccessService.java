package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.DataAccess;
import com.aigovernance.lifecycle.repository.DataAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DataAccessService {

    @Autowired
    private DataAccessRepository accessRepo;

    public void insertAccessManual(DataAccess access) {
        if (accessRepo.existsById(access.getAccessId())) {
            throw new RuntimeException("Duplicate Access ID: " + access.getAccessId());
        }
        accessRepo.insertAccessQuery(access.getAccessId(), access.getAccessType(),
                access.getAccessExpirationDate(), access.getAccessStatus(),
                access.getRoleId(), access.getDatasetId());
    }

    public List<Map<String, Object>> getRules() { return accessRepo.findDatasetAccessRules(); }
    public List<Map<String, Object>> getActive() { return accessRepo.findActivePermissions(); }

    public void runExpiryCheck(LocalDate date) { accessRepo.expireAccessRules(date); }
    public void upgradeAccess(Integer dsId, Integer roleId) { accessRepo.updateAccessTypeByRole(dsId, roleId); }
    public void cleanupExpired() { accessRepo.deleteExpiredRules(); }
    public void removeByRole(String roleName) { accessRepo.deleteRulesByRoleName(roleName); }
}
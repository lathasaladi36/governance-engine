package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.DataAccess;
import com.aigovernance.lifecycle.service.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/data-access")
public class DataAccessController {

    @Autowired
    private DataAccessService accessService;

    @PostMapping // INSERT: [cite: 61-67]
    public ResponseEntity<Map<String, Object>> addAccess(@RequestBody DataAccess access) {
        accessService.insertAccessManual(access);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "Success");
        res.put("message", "DataAccess rule created via native SQL");
        res.put("data", access);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/rules") public List<Map<String, Object>> getRules() { return accessService.getRules(); } // [cite: 104-112]
    @GetMapping("/active") public List<Map<String, Object>> getActive() { return accessService.getActive(); } // [cite: 188-201]
    @GetMapping("/permissions-report")
    public ResponseEntity<List<Map<String, Object>>> getPermissionsReport() {
        return ResponseEntity.ok(accessService.getPermissionsReport());
    }

    @PutMapping("/expire-check") // UPDATE: [cite: 365-373]
    public String expire(@RequestParam String date) {
        accessService.runExpiryCheck(LocalDate.parse(date));
        return "Expiry check completed.";
    }

    @PutMapping("/upgrade") // UPDATE: [cite: 374-381]
    public String upgrade(@RequestParam Integer dsId, @RequestParam Integer roleId) {
        accessService.upgradeAccess(dsId, roleId);
        return "Access upgraded to read_write.";
    }

    @DeleteMapping("/cleanup") public String cleanup() { accessService.cleanupExpired(); return "Expired rules deleted"; } // [cite: 455-460]
    @DeleteMapping("/role/{roleName}") public String deleteByRole(@PathVariable String roleName) {
        accessService.removeByRole(roleName);
        return "Rules for " + roleName + " removed.";
    } // [cite: 461-472]
}
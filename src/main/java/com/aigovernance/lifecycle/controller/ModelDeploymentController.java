package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.ModelDeployment;
import com.aigovernance.lifecycle.service.ModelDeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/deployments")
public class ModelDeploymentController {

    @Autowired
    private ModelDeploymentService deploymentService;

    @PostMapping // [cite: 75-82]
    public ResponseEntity<Map<String, Object>> addDeployment(@RequestBody ModelDeployment deployment) {
        deploymentService.insertDeploymentManual(deployment);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "Success");
        res.put("message", "ModelDeployment created via native SQL");
        res.put("data", deployment);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/all") public List<Map<String, Object>> getAll() { return deploymentService.getDeploymentsByModel(); } // [cite: 134-144]
    @GetMapping("/latest") public List<Map<String, Object>> getLatest() { return deploymentService.getLatestRanked(); } // [cite: 202-226]

    @PutMapping("/{id}/hosting") // [cite: 396-404]
    public String updateHosting(@PathVariable Integer id, @RequestParam String host, @RequestParam String comment) {
        deploymentService.updateHosting(id, host, comment);
        return "Hosting details updated for deployment " + id;
    }

    @DeleteMapping("/cleanup") public String cleanup() {
        deploymentService.cleanupOld();
        return "Deployments older than one year purged successfully.";
    } // [cite: 449-454]
}
package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.Model;
import com.aigovernance.lifecycle.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/models")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @PostMapping // INSERT:
    public ResponseEntity<Map<String, Object>> addModel(@RequestBody Model model) {
        Map<String, Object> response = new HashMap<>();
        try {
            modelService.insertModelManual(model);
            response.put("status", "Success");
            response.put("message", "Model created successfully using native SQL query");
            response.put("data", model);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping // SELECT All: [cite: 92-93]
    public List<Model> getAll() { return modelService.listAll(); }

    @GetMapping("/datasets") // SELECT Join: [cite: 180-186]
    public List<Map<String, Object>> getWithDatasets() { return modelService.getModelDatasetLinks(); }

    @GetMapping("/latest-deployments") // SELECT Join: [cite: 145-154]
    public List<Map<String, Object>> getLatest() { return modelService.getLatestDeployments(); }

    @PutMapping("/{id}/performance") // UPDATE: [cite: 382-388]
    public String updatePerf(@PathVariable Integer id, @RequestParam String perf) {
        modelService.updatePerf(id, perf);
        return "Model performance updated.";
    }

    @DeleteMapping("/cleanup") // DELETE Conditional: [cite: 432-443]
    public String cleanup() {
        modelService.cleanupModels();
        return "Models with no deployment history removed.";
    }
}
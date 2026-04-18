package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.SourceOfData;
import com.aigovernance.lifecycle.service.SourceOfDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sources")
public class SourceOfDataController {

    @Autowired
    private SourceOfDataService sourceService;

    @PostMapping // INSERT: [cite: 31-38]
    public ResponseEntity<Map<String, Object>> addSource(@RequestBody SourceOfData source) {
        Map<String, Object> response = new HashMap<>();
        try {
            sourceService.insertSourceManual(source);
            response.put("status", "Success");
            response.put("message", "SourceOfData created successfully using native SQL query");
            response.put("data", source);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
    // Inside SourceOfDataController.java
    @GetMapping
    public ResponseEntity<List<SourceOfData>> getAllSources() {
        List<SourceOfData> sources = sourceService.listAll();
        return ResponseEntity.ok(sources);
    }

    @GetMapping("/stats") // SELECT Aggregate: [cite: 171-179]
    public List<Map<String, Object>> getStats() {
        return sourceService.getSourceStats();
    }

    @PutMapping("/{id}/refresh") // UPDATE: [cite: 297-307]
    public String refresh(@PathVariable Integer id, @RequestParam String location, @RequestParam String details) {
        sourceService.updateSourceInfo(id, location, details);
        return "Source " + id + " refreshed successfully.";
    }

    @DeleteMapping("/cleanup") // DELETE: [cite: 507-518]
    public String cleanup() {
        sourceService.cleanupSources();
        return "Sources with no associated datasets removed.";
    }
}
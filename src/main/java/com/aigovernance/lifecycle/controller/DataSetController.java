package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.DataSet;
import com.aigovernance.lifecycle.service.DataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/datasets")
public class DataSetController {

    @Autowired
    private DataSetService dataSetService;

    @PostMapping // [cite: 40-47]
    public ResponseEntity<Map<String, Object>> addDataSet(@RequestBody DataSet dataSet) {
        dataSetService.insertDataSetManual(dataSet);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "Success");
        res.put("message", "DataSet created via native SQL query");
        res.put("data", dataSet);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public List<DataSet> getDatasets() { return dataSetService.listAll(); }

    @GetMapping("/by-source") public List<Map<String, Object>> getBySource() { return dataSetService.getBySource(); } // [cite: 96-103]

    @PutMapping("/{id}/cleanup") // [cite: 315-323]
    public String cleanup(@PathVariable Integer id, @RequestParam Double size, @RequestParam String desc) {
        dataSetService.updateCleanup(id, size, desc);
        return "DataSet updated after cleanup.";
    }
    @DeleteMapping("/{id}") // [cite: 420-431]
    public String deleteByDataset(@PathVariable Integer id) {
        dataSetService.removeByDataset(id);
        return "Datasets with " + id + " deleted.";
    }

    @DeleteMapping("/source/{name}") // [cite: 420-431]
    public String deleteBySource(@PathVariable String name) {
        dataSetService.removeBySource(name);
        return "Datasets for source " + name + " deleted.";
    }
}
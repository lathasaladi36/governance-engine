package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.SourceOfData;
import com.aigovernance.lifecycle.repository.SourceOfDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class SourceOfDataService {

    @Autowired
    private SourceOfDataRepository sourceRepository;
    // Inside SourceOfDataService.java
    public List<SourceOfData> listAll() {
        return sourceRepository.findAllSourcesQuery();
    }

    public void insertSourceManual(SourceOfData source) {
        // Duplicate Logic: Check if Source ID already exists
        if (sourceRepository.existsById(source.getSourceId())) {
            throw new RuntimeException("Duplicate Error: Source ID " + source.getSourceId() + " already exists.");
        }
        sourceRepository.insertSourceQuery(
                source.getSourceId(),
                source.getDatasourceName(),
                source.getDatasourceType(),
                source.getDatasourceDetails(),
                source.getDatasourceLocation()
        );
    }

    public List<Map<String, Object>> getSourceStats() {
        return sourceRepository.findDatasetCountsPerSource();
    }

    public void updateSourceInfo(Integer id, String location, String details) {
        sourceRepository.refreshSource(id, location, details);
    }

    public void cleanupSources() {
        sourceRepository.deleteEmptySources();
    }
}
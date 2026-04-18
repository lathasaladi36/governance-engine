package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.DataSet;
import com.aigovernance.lifecycle.repository.DataSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class DataSetService {

    @Autowired
    private DataSetRepository dataSetRepo;

    public List<DataSet> listAll() { return dataSetRepo.findAllDatasetsQuery(); }

    // INSERT logic with Duplicate Check [cite: 40-47]
    public void insertDataSetManual(DataSet dataSet) {
        // Governance check: Ensure the dataset_id is unique before inserting
        if (dataSetRepo.existsById(dataSet.getDatasetId())) {
            throw new RuntimeException("Duplicate Error: DataSet ID " + dataSet.getDatasetId() + " already exists in the repository.");
        }

        dataSetRepo.insertDataSetQuery(
                dataSet.getDatasetId(),
                dataSet.getDatasetType(),
                dataSet.getDatasetLabel(),
                dataSet.getDatasetFormat(),
                dataSet.getDatasetSize(),
                dataSet.getDatasetDescription(),
                dataSet.getSourceOfData().getSourceId() // FIX: Access ID via the object
        );
    }

    // SELECT: Datasets categorized by Source [cite: 96-103]
    public List<Map<String, Object>> getBySource() {
        return dataSetRepo.findDatasetsBySource();
    }

    // SELECT: Access rules for datasets [cite: 104-112]
    public List<Map<String, Object>> getAccessRules() {
        return dataSetRepo.findDatasetAccessRules();
    }

    // UPDATE: Maintain data integrity after cleanup [cite: 315-323]
    public void updateCleanup(Integer id, Double size, String desc) {
        dataSetRepo.updateCleanupQuery(id, size, desc);
    }

    // UPDATE: Standardize format names (e.g., Parquet) [cite: 331-339]
    public void normalizeAllFormats() {
        dataSetRepo.normalizeFormats();
    }

    // DELETE: Remove all datasets linked to a specific source [cite: 420-431]
    public void removeBySource(String sourceName) {
        dataSetRepo.deleteBySourceName(sourceName);
    }
}
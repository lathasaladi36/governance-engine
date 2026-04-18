package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.Model;
import com.aigovernance.lifecycle.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepo;

    public void insertModelManual(Model model) {
        if (modelRepo.existsById(model.getModelId())) {
            throw new RuntimeException("Duplicate Error: Model ID " + model.getModelId() + " already exists.");
        }
        modelRepo.insertModelQuery(
                model.getModelId(),
                model.getModelName(),
                model.getModelType(),
                model.getModelPerformance(),
                model.getDataSet().getDatasetId() // Fix: Access the ID through the object
        );
    }

    public List<Model> listAll() { return modelRepo.findAllModelsQuery(); }
    public List<Map<String, Object>> getModelDatasetLinks() { return modelRepo.findModelsWithDatasets(); }
    public List<Map<String, Object>> getLatestDeployments() { return modelRepo.findLatestDeployments(); }

    public void updatePerf(Integer id, String perf) { modelRepo.updatePerformanceQuery(id, perf); }
    public void updateType(Integer id, String type) { modelRepo.updateModelTypeQuery(id, type); }
    public void cleanupModels() { modelRepo.deleteModelsWithNoDeploymentsQuery(); }
}
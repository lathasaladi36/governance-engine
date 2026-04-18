package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.ModelDeployment;
import com.aigovernance.lifecycle.repository.ModelDeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ModelDeploymentService {

    @Autowired
    private ModelDeploymentRepository deploymentRepo;

    public void insertDeploymentManual(ModelDeployment d) {
        if (deploymentRepo.existsById(d.getDeploymentId())) {
            throw new RuntimeException("Duplicate Error: Deployment ID " + d.getDeploymentId() + " already exists.");
        }
        deploymentRepo.insertDeploymentQuery(
                d.getDeploymentId(), d.getModelVersion(), d.getTestingId(),
                d.getDeploymentDate(), d.getDeploymentComments(), d.getModel().getModelId()
        );
    }

    public List<Map<String, Object>> getDeploymentsByModel() { return deploymentRepo.findDeploymentsByModel(); }
    public List<Map<String, Object>> getLatestRanked() { return deploymentRepo.findLatestRankedDeployments(); }

    public void updateHosting(Integer id, String host, String comment) {
        deploymentRepo.updateHostingDetails(id, host, comment);
    }

    public void cleanupOld() { deploymentRepo.deleteOldDeploymentsQuery(); }
}
package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    

    // 1. INSERT using native query [cite: 69-74]
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO models (model_id, model_name, model_type, model_performance, dataset_id) " +
            "VALUES (:id, :name, :type, :perf, :dsId)", nativeQuery = true)
    void insertModelQuery(@Param("id") Integer id, @Param("name") String name,
                          @Param("type") String type, @Param("perf") String perf,
                          @Param("dsId") Integer dsId);

    // 2. SELECT: List all Models [cite: 92-93]
    @Query(value = "SELECT * FROM models", nativeQuery = true)
    List<Model> findAllModelsQuery();

    // 3. SELECT: List details of Models and Datasets they depend on [cite: 180-186]
    @Query(value = "SELECT d.dataset_label, m.model_name, m.model_type " +
            "FROM dataset d JOIN models m ON d.dataset_id = m.dataset_id", nativeQuery = true)
    List<Map<String, Object>> findModelsWithDatasets();

    // 4. SELECT: Latest deployment for each model (Join + Order) [cite: 145-154]
    @Query(value = "SELECT m.model_name, d.model_version, d.deployment_date " +
            "FROM models m JOIN model_deployment d ON m.model_id = d.model_id " +
            "ORDER BY m.model_id, d.deployment_date DESC", nativeQuery = true)
    List<Map<String, Object>> findLatestDeployments();

    // 5. UPDATE: Models performance text [cite: 382-388]
    @Modifying
    @Transactional
    @Query(value = "UPDATE models SET model_performance = :perf WHERE model_id = :id", nativeQuery = true)
    void updatePerformanceQuery(@Param("id") Integer id, @Param("perf") String perf);

    // 6. UPDATE: Model type [cite: 389-395]
    @Modifying
    @Transactional
    @Query(value = "UPDATE models SET model_type = :type WHERE model_id = :id", nativeQuery = true)
    void updateModelTypeQuery(@Param("id") Integer id, @Param("type") String type);

    // 7. DELETE: Model with no deployments [cite: 432-443]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM models m WHERE NOT EXISTS " +
            "(SELECT 1 FROM model_deployment md WHERE md.model_id = m.model_id)", nativeQuery = true)
    void deleteModelsWithNoDeploymentsQuery();
}
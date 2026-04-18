package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.ModelDeployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface ModelDeploymentRepository extends JpaRepository<ModelDeployment, Integer> {

    // 1. INSERT [cite: 75-82]
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO model_deployment (deployment_id, model_version, testing_id, deployment_date, deployment_comments, model_id) " +
            "VALUES (:id, :version, :testId, :date, :comments, :modelId)", nativeQuery = true)
    void insertDeploymentQuery(@Param("id") Integer id, @Param("version") String version,
                               @Param("testId") String testId, @Param("date") LocalDate date,
                               @Param("comments") String comments, @Param("modelId") Integer modelId);

    // 2. SELECT: List deployments for each model [cite: 134-144]
    @Query(value = "SELECT m.model_name, d.deployment_id, d.model_version, d.deployment_date, d.deployment_comments " +
            "FROM models m JOIN model_deployment d ON m.model_id = d.model_id", nativeQuery = true)
    List<Map<String, Object>> findDeploymentsByModel();

    // 3. SELECT: Latest deployment using Window Function (RANK) [cite: 202-226]
    @Query(value = "WITH ranked AS (SELECT md.*, ROW_NUMBER() OVER(PARTITION BY md.model_id ORDER BY md.deployment_date DESC, md.deployment_id DESC) AS rn " +
            "FROM model_deployment md) " +
            "SELECT m.model_name, r.model_version, r.deployment_date, r.hosted_in " +
            "FROM ranked r JOIN models m ON m.model_id = r.model_id " +
            "WHERE r.rn = 1 ORDER BY m.model_name", nativeQuery = true)
    List<Map<String, Object>> findLatestRankedDeployments();

    // 4. UPDATE: Hosting target and comments [cite: 396-404]
    @Modifying
    @Transactional
    @Query(value = "UPDATE model_deployment SET hosted_in = :host, deployment_comments = :comment " +
            "WHERE deployment_id = :id", nativeQuery = true)
    void updateHostingDetails(@Param("id") Integer id, @Param("host") String host, @Param("comment") String comment);

    // 5. DELETE: Older than 1 year [cite: 449-454]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM model_deployment WHERE deployment_date < CURRENT_DATE - INTERVAL '1 year'", nativeQuery = true)
    void deleteOldDeploymentsQuery();
}
package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.SourceOfData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface SourceOfDataRepository extends JpaRepository<SourceOfData, Integer> {

    // Inside SourceOfDataRepository.java
    @Query(value = "SELECT * FROM sourceofdata", nativeQuery = true)
    List<SourceOfData> findAllSourcesQuery();

    // 1. INSERT using native query [cite: 31-38]
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO sourceofdata (source_id, datasource_name, datasource_type, datasource_details, datasource_location) " +
            "VALUES (:id, :name, :type, :details, :location)", nativeQuery = true)
    void insertSourceQuery(@Param("id") Integer id,
                           @Param("name") String name,
                           @Param("type") String type,
                           @Param("details") String details,
                           @Param("location") String location);

    // 2. List of the no. of Datasets that each DataSource supplies [cite: 171-179]
    @Query(value = "SELECT s.datasource_name, COUNT(d.dataset_id) AS total_datasets " +
            "FROM sourceofdata s LEFT JOIN dataset d ON s.source_id = d.source_id " +
            "GROUP BY s.datasource_name", nativeQuery = true)
    List<Map<String, Object>> findDatasetCountsPerSource();

    // 3. Refresh all the Datasource location and details [cite: 297-307]
    @Modifying
    @Transactional
    @Query(value = "UPDATE sourceofdata SET datasource_location = :location, datasource_details = :details " +
            "WHERE source_id = :id", nativeQuery = true)
    void refreshSource(@Param("id") Integer id, @Param("location") String location, @Param("details") String details);

    // 4. Delete a source with no datasets [cite: 507-518]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sourceofdata s WHERE NOT EXISTS " +
            "(SELECT 1 FROM dataset d WHERE d.source_id = s.source_id)", nativeQuery = true)
    void deleteEmptySources();
}
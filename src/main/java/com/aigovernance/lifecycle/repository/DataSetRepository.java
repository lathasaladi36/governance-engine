package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.DataSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Integer> {

    @Query(value = "SELECT * FROM dataset", nativeQuery = true/*2*/)
    List<DataSet> findAllDatasetsQuery();

    // 1. INSERT [cite: 40-47] 1
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO dataset (dataset_id, dataset_type, dataset_label, dataset_format, dataset_size, dataset_description, source_id) " +
            "VALUES (:id, :type, :label, :format, :size, :desc, :sourceId)", nativeQuery = true)
    void insertDataSetQuery(@Param("id") Integer id, @Param("type") String type, @Param("label") String label,
                            @Param("format") String format, @Param("size") Double size,
                            @Param("desc") String desc, @Param("sourceId") Integer sourceId);

    // 2. SELECT: List out all the datasets provided by each data source [cite: 96-103]/*3*/
    @Query(value = "SELECT s.datasource_name, d.dataset_id, d.dataset_label " +
            "FROM sourceofdata s JOIN dataset d ON s.source_id = d.source_id", nativeQuery = true)
    List<Map<String, Object>> findDatasetsBySource();

    // 3. SELECT: List access rules for each dataset [cite: 104-112]/*4*/
    @Query(value = "SELECT da.access_id, da.access_type, d.dataset_id, d.dataset_label " +
            "FROM dataaccess da JOIN dataset d ON da.dataset_id = d.dataset_id", nativeQuery = true)
    List<Map<String, Object>> findDatasetAccessRules();

    // 4. UPDATE: Description and size following clean up [cite: 315-323]/*5*/
    @Modifying
    @Transactional
    @Query(value = "UPDATE dataset SET dataset_size = :size, dataset_description = :desc WHERE dataset_id = :id", nativeQuery = true)
    void updateCleanupQuery(@Param("id") Integer id, @Param("size") Double size, @Param("desc") String desc);

    // 5. UPDATE: Normalize format names [cite: 331-339] /*6*/
    @Modifying
    @Transactional
    @Query(value = "UPDATE dataset SET dataset_format = 'parquet' WHERE LOWER(dataset_format) IN ('pq', 'parq', 'parquet')", nativeQuery = true)
    void normalizeFormats();
    /*Change dataset’s upstream source*/
    @Modifying
    @Query(value = "UPDATE dataset SET source_id = :sourceId WHERE dataset_id = :datasetId", nativeQuery = true)
    void updateSourceIdNative(@Param("datasetId") Integer datasetId, @Param("sourceId") Integer sourceId);

    // 6. DELETE: Delete all datasets from a particular source [cite: 420-431]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dataset WHERE source_id = (SELECT source_id FROM sourceofdata WHERE datasource_name = :sourceName)", nativeQuery = true)
    void deleteBySourceName(@Param("sourceName") String sourceName);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dataset WHERE dataset_id = :id ", nativeQuery = true)
    void deleteByDatasetid(Integer id);
}
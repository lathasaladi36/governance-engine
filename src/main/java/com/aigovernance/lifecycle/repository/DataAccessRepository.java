package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.DataAccess;
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
public interface DataAccessRepository extends JpaRepository<DataAccess, Integer> {

    // 1. INSERT using native query
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO dataaccess (access_id, access_type, access_expiration_date, access_status, role_id, dataset_id) " +
            "VALUES (:id, :type, :expDate, :status, :roleId, :dsId)", nativeQuery = true)
    void insertAccessQuery(@Param("id") Integer id, @Param("type") String type,
                           @Param("expDate") LocalDate expDate, @Param("status") String status,
                           @Param("roleId") Integer roleId, @Param("dsId") Integer dsId);

    // 2. SELECT: List access rules for each dataset [cite: 104-112]
    @Query(value = "SELECT da.access_id, da.access_type, da.access_status, d.dataset_id, d.dataset_type " +
            "FROM dataaccess da JOIN dataset d ON da.dataset_id = d.dataset_id", nativeQuery = true)
    List<Map<String, Object>> findDatasetAccessRules();
/*List of particular users have access to which datasets*/
    @Query(value = "SELECT u.username, r.role_name, d.dataset_label as dataset_name, " +
            "da.access_type, da.access_status " +
            "FROM dataaccess da " +
            "JOIN role r ON da.role_id = r.role_id " +
            "JOIN user_account u ON u.role_id = r.role_id " +
            "JOIN dataset d ON da.dataset_id = d.dataset_id",
            nativeQuery = true)
    List<Map<String, Object>> findDetailedPermissionsNative();

    // 3. SELECT: Active roles of Datasets permissions [cite: 188-201]
    @Query(value = "SELECT r.role_name, d.dataset_type, da.permission, da.access_status " +
            "FROM dataaccess da JOIN role r ON r.role_id = da.role_id " +
            "JOIN dataset d ON d.dataset_id = da.dataset_id " +
            "WHERE da.access_status = 'active' ORDER BY r.role_name", nativeQuery = true)
    List<Map<String, Object>> findActivePermissions();

    // 4. UPDATE: Access status to expired depending upon date [cite: 365-373]
    @Modifying
    @Transactional
    @Query(value = "UPDATE dataaccess SET access_status = 'expired' " +
            "WHERE access_type = 'read' AND access_expiration_date < :checkDate", nativeQuery = true)
    void expireAccessRules(@Param("checkDate") LocalDate checkDate);

    // 5. UPDATE: Access type depending upon role [cite: 374-381]
    @Modifying
    @Transactional
    @Query(value = "UPDATE dataaccess SET access_type = 'read_write' " +
            "WHERE dataset_id = :dsId AND role_id = :roleId", nativeQuery = true)
    void updateAccessTypeByRole(@Param("dsId") Integer dsId, @Param("roleId") Integer roleId);

    // 6. DELETE: Expired access rules [cite: 455-460]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dataaccess WHERE access_expiration_date < CURRENT_DATE AND access_status = 'expired'", nativeQuery = true)
    void deleteExpiredRules();

    // 7. DELETE: Permissions associated with a particular role name [cite: 461-472]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dataaccess WHERE role_id = (SELECT role_id FROM role WHERE role_name = :roleName)", nativeQuery = true)
    void deleteRulesByRoleName(@Param("roleName") String roleName);
}
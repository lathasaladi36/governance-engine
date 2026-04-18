package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM role", nativeQuery = true)
    List<Role> findAllRolesQuery();

    // 1. INSERT [cite: 49-53]
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO role (role_id, role_name, role_description) VALUES (:id, :name, :description)", nativeQuery = true)
    void insertRoleQuery(@Param("id") Integer id, @Param("name") String name, @Param("description") String description);

    // 2. SELECT: List users and their roles [cite: 113-120]
    @Query(value = "SELECT u.user_id, u.username, r.role_name, r.role_description FROM user_account u JOIN role r ON u.role_id = r.role_id", nativeQuery = true)
    List<Map<String, Object>> findUsersAndRoles();

    // 3. SELECT: Particular users access to datasets [cite: 121-133]
    @Query(value = "SELECT u.username, r.role_name, d.dataset_label, da.access_type, da.access_status " +
            "FROM dataaccess da JOIN role r ON da.role_id = r.role_id " +
            "JOIN user_account u ON u.role_id = r.role_id " +
            "JOIN dataset d ON da.dataset_id = d.dataset_id", nativeQuery = true)
    List<Map<String, Object>> findUserDatasetAccess();

    // 4. SELECT: Roles of Datasets permissions active [cite: 188-201]
    @Query(value = "SELECT r.role_name, d.dataset_label, da.access_type, da.access_status " +
            "FROM dataaccess da JOIN role r ON r.role_id = da.role_id " +
            "JOIN dataset d ON d.dataset_id = da.dataset_id " +
            "WHERE da.access_status = 'active' ORDER BY r.role_name, d.dataset_label", nativeQuery = true)
    List<Map<String, Object>> findActiveRolePermissions();

    // 5. SELECT: Count roles types per dataset [cite: 227-253]
    @Query(value = "SELECT d.dataset_label, SUM(CASE WHEN da.access_type = 'admin' THEN 1 ELSE 0 END) AS admin_roles, " +
            "SUM(CASE WHEN da.access_type = 'read' THEN 1 ELSE 0 END) AS read_roles " +
            "FROM dataset d LEFT JOIN dataaccess da ON da.dataset_id = d.dataset_id " +
            "AND da.access_status = 'active' GROUP BY d.dataset_label", nativeQuery = true)
    List<Map<String, Object>> findRoleCountsPerDataset();

    // 6. SELECT: Users with particular permissions [cite: 254-261]
    @Query(value = "SELECT u.user_id, u.username, r.role_name FROM user_account u JOIN role r ON r.role_id = u.role_id " +
            "WHERE EXISTS (SELECT 1 FROM dataaccess da WHERE da.role_id = r.role_id AND da.access_status = 'active')", nativeQuery = true)
    List<Map<String, Object>> findPrivilegedUsers();

    // 7. UPDATE: role name & description [cite: 340-349]
    @Modifying
    @Transactional
    @Query(value = "UPDATE role SET role_name = :name, role_description = :description WHERE role_id = :id", nativeQuery = true)
    void updateRoleQuery(@Param("id") Integer id, @Param("name") String name, @Param("description") String description);

    // 8. DELETE: Roles with no users mapped [cite: 490-501]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM role r WHERE NOT EXISTS (SELECT 1 FROM user_account u WHERE u.role_id = r.role_id)", nativeQuery = true)
    void deleteUnusedRolesQuery();

    // 9. DELETE: Role by name [cite: 502-506]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM role WHERE role_name = :name", nativeQuery = true)
    void deleteRoleByNameQuery(@Param("name") String name);
}
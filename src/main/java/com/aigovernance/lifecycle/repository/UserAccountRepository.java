package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    @Query(value = "SELECT * FROM user_account", nativeQuery = true)
    List<UserAccount> findAllUsersQuery(); //

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_account (user_id, username, email_id, role_id) " +
            "VALUES (:id, :name, :email, :roleId)", nativeQuery = true)
    void insertUserQuery(@Param("id") Integer id,
                         @Param("name") String name,
                         @Param("email") String email,
                         @Param("roleId") Integer roleId);


    // 1. List users and their roles [cite: 113-120]
    @Query(value = "SELECT u.user_id, u.username, r.role_name, r.role_description " +
            "FROM user_account u JOIN role r ON u.role_id = r.role_id", nativeQuery = true)
    List<Map<String, Object>> findUsersWithRoles();

    // 2. Users who logged in but never logged out [cite: 163-170]
    @Query(value = "SELECT u.username, s.session_id, s.login_time " +
            "FROM usersession s JOIN user_account u ON s.user_id = u.user_id " +
            "WHERE s.logout_time IS NULL", nativeQuery = true)
    List<Map<String, Object>> findActiveUsers();

    // 3. Update username [cite: 350-356]
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_account SET username = :newName WHERE user_id = :id", nativeQuery = true)
    void updateUserName(@Param("id") Integer id, @Param("newName") String newName);

    // 4. Delete user by username [cite: 485-489]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_account WHERE username = :username", nativeQuery = true)
    void deleteByUsername(@Param("username") String username);

    // 5. Delete user who has no sessions [cite: 473-484]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_account u WHERE NOT EXISTS " +
            "(SELECT 1 FROM usersession s WHERE s.user_id = u.user_id)", nativeQuery = true)
    void deleteInactiveUsers();
}
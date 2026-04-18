package com.aigovernance.lifecycle.repository;

import com.aigovernance.lifecycle.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {

    // 1. INSERT using native query [cite: 84-88]
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO usersession (session_id, login_time, logout_time, user_id) " +
            "VALUES (:id, :login, :logout, :userId)", nativeQuery = true)
    void insertSessionQuery(@Param("id") Integer id,
                            @Param("login") LocalDateTime login,
                            @Param("logout") LocalDateTime logout,
                            @Param("userId") Integer userId);

    // 2. List login activity for all users [cite: 155-162]
    @Query(value = "SELECT u.username, s.session_id, s.login_time, s.logout_time " +
            "FROM user_account u JOIN usersession s ON u.user_id = s.user_id", nativeQuery = true)
    List<Map<String, Object>> findAllLoginActivity();

    // 3. List Users who logged in but never logged out [cite: 163-170]
    @Query(value = "SELECT u.username, s.session_id, s.login_time " +
            "FROM usersession s JOIN user_account u ON s.user_id = u.user_id " +
            "WHERE s.logout_time IS NULL", nativeQuery = true)
    List<Map<String, Object>> findActiveSessions();

    // 4. Update/Close a session that was not logged out [cite: 405-412]
    @Modifying
    @Transactional
    @Query(value = "UPDATE usersession SET logout_time = :logout WHERE session_id = :id AND logout_time IS NULL", nativeQuery = true)
    void closeSession(@Param("id") Integer id, @Param("logout") LocalDateTime logout);

    // 5. Delete sessions older than 30 days [cite: 519-523]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM usersession WHERE logout_time < NOW() - INTERVAL '30 days'", nativeQuery = true)
    void deleteOldSessions();

    // 6. Delete all open sessions [cite: 524-528]
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM usersession WHERE logout_time IS NULL", nativeQuery = true)
    void deleteAllOpenSessions();
}
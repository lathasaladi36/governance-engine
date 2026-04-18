package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.UserSession;
import com.aigovernance.lifecycle.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository sessionRepository;

    public void insertSessionManual(UserSession session) {
        // Duplicate Logic: Check if Session ID exists [cite: 15]
        if (sessionRepository.existsById(session.getSessionId())) {
            throw new RuntimeException("Duplicate Error: Session ID " + session.getSessionId() + " already exists.");
        }
        sessionRepository.insertSessionQuery(
                session.getSessionId(),
                session.getLoginTime(),
                session.getLogoutTime(),
                session.getUser().getUserId()
        );
    }

    public List<Map<String, Object>> getAllActivity() { return sessionRepository.findAllLoginActivity(); }

    public List<Map<String, Object>> getActive() { return sessionRepository.findActiveSessions(); }

    public void closeActiveSession(Integer id, LocalDateTime logout) { sessionRepository.closeSession(id, logout); }

    public void purgeOldSessions() { sessionRepository.deleteOldSessions(); }

    public void clearOpenSessions() { sessionRepository.deleteAllOpenSessions(); }
}
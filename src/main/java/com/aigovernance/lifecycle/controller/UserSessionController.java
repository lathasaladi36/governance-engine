package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.UserSession;
import com.aigovernance.lifecycle.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class UserSessionController {

    @Autowired
    private UserSessionService sessionService;

    @PostMapping // INSERT: [cite: 83-88]
    public ResponseEntity<Map<String, Object>> addSession(@RequestBody UserSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            sessionService.insertSessionManual(session);
            response.put("status", "Success");
            response.put("message", "UserSession created successfully using native SQL query");
            response.put("data", session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @GetMapping // SELECT All activity [cite: 155-162]
    public List<Map<String, Object>> listAll() { return sessionService.getAllActivity(); }

    @GetMapping("/active") // SELECT Active sessions [cite: 163-170]
    public List<Map<String, Object>> listActive() { return sessionService.getActive(); }

    @PutMapping("/{id}/close") // UPDATE: Close session [cite: 405-412]
    public String close(@PathVariable Integer id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime logout) {
        sessionService.closeActiveSession(id, logout);
        return "Session " + id + " closed successfully.";
    }

    @DeleteMapping("/purge") // DELETE: Older than 30 days [cite: 519-523]
    public String purge() {
        sessionService.purgeOldSessions();
        return "Old sessions purged.";
    }

    @DeleteMapping("/clear-open") // DELETE: All open sessions [cite: 524-528]
    public String clearOpen() {
        sessionService.clearOpenSessions();
        return "All open sessions cleared.";
    }
}
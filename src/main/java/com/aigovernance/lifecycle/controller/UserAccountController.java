package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.UserAccount;
import com.aigovernance.lifecycle.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    @Autowired
    private UserAccountService userService;

    // POST: Insert a new user [cite: 54-60]
    @PostMapping
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody UserAccount user) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.insertUserManual(user);

            response.put("status", "Success");
            response.put("message", "UserAccount created successfully");
            response.put("data", user);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // This catches the duplicate logic from the service
            response.put("status", "Duplicate Blocked");
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    // GET: List all users [cite: 94-95]
    @GetMapping
    public List<UserAccount> listAll() {
        return userService.getAllUsers();
    }

    // GET: List users and their roles [cite: 113-120]
    @GetMapping("/roles")
    public List<Map<String, Object>> listWithRoles() {
        return userService.getUsersWithRoles();
    }

    // GET: Users who never logged out [cite: 163-170]
    @GetMapping("/active")
    public List<Map<String, Object>> listActive() {
        return userService.getActiveUsers();
    }

    // PUT: Change username [cite: 350-356]
    @PutMapping("/{id}/username")
    public String changeUsername(@PathVariable Integer id, @RequestParam String name) {
        userService.updateUsername(id, name);
        return "User name updated successfully";
    }

    // DELETE: Delete by username [cite: 485-489]
    @DeleteMapping("/name/{username}")
    public String deleteByName(@PathVariable String username) {
        userService.deleteByUsername(username);
        return "User " + username + " deleted.";
    }

    // DELETE: Cleanup inactive users [cite: 473-484]
    @DeleteMapping("/cleanup")
    public String cleanup() {
        userService.cleanupUsers();
        return "Inactive users removed from governance engine.";
    }
}
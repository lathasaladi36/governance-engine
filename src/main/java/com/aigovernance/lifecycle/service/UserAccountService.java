package com.aigovernance.lifecycle.service;

import com.aigovernance.lifecycle.model.UserAccount;
import com.aigovernance.lifecycle.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userRepository;

    public List<UserAccount> listAll() { return userRepository.findAllUsersQuery(); }

    // INSERT logic [cite: 54-60]
    public void insertUserManual(UserAccount user) {
        // DUPLICATE LOGIC: Check if ID already exists before running the INSERT
        if (userRepository.existsById(user.getUserId())) {
            throw new RuntimeException("Duplicate Error: User ID " + user.getUserId() + " is already registered.");
        }

        // Only runs if the user is unique
        userRepository.insertUserQuery(
                user.getUserId(),
                user.getUsername(),
                user.getEmailId(),
                user.getRole().getRoleId()
        );
    }

    public List<UserAccount> getAllUsers() { return userRepository.findAll(); } // [cite: 94-95]

    public List<Map<String, Object>> getUsersWithRoles() { return userRepository.findUsersWithRoles(); }

    public List<Map<String, Object>> getActiveUsers() { return userRepository.findActiveUsers(); }

    public void updateUsername(Integer id, String name) { userRepository.updateUserName(id, name); }

    public void deleteByUsername(String username) { userRepository.deleteByUsername(username); }

    public void cleanupUsers() { userRepository.deleteInactiveUsers(); }
}
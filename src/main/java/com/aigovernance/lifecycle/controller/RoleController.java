package com.aigovernance.lifecycle.controller;

import com.aigovernance.lifecycle.model.Role;
import com.aigovernance.lifecycle.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping //
    public ResponseEntity<Map<String, Object>> addRole(@RequestBody Role role) {
        roleService.insertRole(role);
        Map<String, Object> res = new HashMap<>();
        res.put("status", "Success");
        res.put("message", "Role created via native SQL");
        res.put("data", role);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public List<Role> getRoles() { return roleService.listAll(); }
    @GetMapping("/users") public List<Map<String, Object>> getUsersRoles() { return roleService.getUsersRoles(); } // [cite: 113-120]
    @GetMapping("/active") public List<Map<String, Object>> getActive() { return roleService.getActivePermissions(); } // [cite: 188-201]
    @GetMapping("/stats") public List<Map<String, Object>> getStats() { return roleService.getRoleStats(); } // [cite: 227-253]
    @GetMapping("/privileged") public List<Map<String, Object>> getPrivileged() { return roleService.getPrivilegedUsers(); } // [cite: 254-261]

    @PutMapping("/{id}") // [cite: 340-349]
    public String update(@PathVariable Integer id, @RequestParam String name, @RequestParam String desc) {
        roleService.updateRole(id, name, desc);
        return "Role updated";
    }

    @DeleteMapping("/cleanup") public String cleanup() { roleService.cleanupRoles(); return "Unused roles deleted"; } // [cite: 490-501]
    @DeleteMapping("/name/{name}") public String delete(@PathVariable String name) { roleService.removeRole(name); return "Role deleted"; } // [cite: 502-506]
}
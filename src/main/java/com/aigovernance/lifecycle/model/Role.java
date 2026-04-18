package com.aigovernance.lifecycle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "role") // Matches your DBeaver screenshot
@Data
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "role_id") // [cite: 50]
    private Integer roleId;

    @Column(name = "role_name") // [cite: 51]
    private String roleName;

    @Column(name = "role_description") // [cite: 52]
    private String roleDescription;

    // Link back to UserAccount [cite: 119-120]
    @OneToMany(mappedBy = "role")
    private List<UserAccount> users;
}
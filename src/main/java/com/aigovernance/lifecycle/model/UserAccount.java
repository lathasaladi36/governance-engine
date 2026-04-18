package com.aigovernance.lifecycle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "\"user_account\"") // [cite: 55, 95]
@Data
@NoArgsConstructor
public class UserAccount {

    @Id
    @Column(name = "user_id") // [cite: 56, 115]
    private Integer userId;

    @Column(name = "username") // [cite: 57, 116]
    private String username;

    @Column(name = "email_ID") // [cite: 58]
    private String emailId;

    // Link to the Role table [cite: 59, 119-120]
    @ManyToOne
    @JoinColumn(name = "role_ID")
    private Role role;

    // Link to UserSession for tracking login activity [cite: 161-162]
    @OneToMany(mappedBy = "user")
    private List<UserSession> sessions;
}
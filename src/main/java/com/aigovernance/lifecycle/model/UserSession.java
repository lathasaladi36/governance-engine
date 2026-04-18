package com.aigovernance.lifecycle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "usersession")
@Data
@NoArgsConstructor
public class UserSession {

    @Id
    @Column(name = "session_id") //
    private Integer sessionId;

    @Column(name = "login_time") //
    private LocalDateTime loginTime;

    @Column(name = "logout_time") //
    private LocalDateTime logoutTime;

    @ManyToOne
    @JoinColumn(name = "user_id") //
    private UserAccount user;
}
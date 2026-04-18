package com.aigovernance.lifecycle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "dataaccess") // Matches your database schema [cite: 61]
@Data
@NoArgsConstructor
public class DataAccess {

    @Id
    @Column(name = "access_id")
    private Integer accessId;

    @Column(name = "access_type") // e.g., 'read', 'write' [cite: 64]
    private String accessType;

    @Column(name = "permission") // e.g., 'admin access' [cite: 192]
    private String permission;

    @Column(name = "access_expiration_date") // [cite: 65]
    private LocalDate accessExpirationDate;

    @Column(name = "access_status") // e.g., 'active', 'expired' [cite: 66]
    private String accessStatus;

    @Column(name = "role_id") // Link to Role [cite: 22]
    private Integer roleId;

    @Column(name = "dataset_id") // Link to Dataset [cite: 22]
    private Integer datasetId;
}
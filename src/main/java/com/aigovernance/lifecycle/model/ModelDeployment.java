package com.aigovernance.lifecycle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "model_deployment")
@Data
@NoArgsConstructor
public class ModelDeployment {

    @Id
    @Column(name = "deployment_id")
    private Integer deploymentId;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "testing_id")
    private String testingId;

    @Column(name = "deployment_date")
    private LocalDate deploymentDate;

    @Column(name = "deployment_comments")
    private String deploymentComments;

    @Column(name = "hosted_in") // Used for updates [cite: 400]
    private String hostedIn;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;
}
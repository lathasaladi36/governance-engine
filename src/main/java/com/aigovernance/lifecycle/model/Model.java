package com.aigovernance.lifecycle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "models")
@Data
@NoArgsConstructor
public class Model {
    @Id
    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_type")
    private String modelType;

    @Column(name = "model_performance")
    private String modelPerformance;

    @ManyToOne
    @JoinColumn(name = "dataset_id")
    @JsonIgnore
    private DataSet dataSet; // Renamed to fix mapping

    @OneToMany(mappedBy = "model")
    @JsonBackReference
    private List<ModelDeployment> deployments;
}
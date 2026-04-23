package com.aigovernance.lifecycle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    // Back link to DataSet
    @ManyToOne
    @JoinColumn(name = "dataset_id")
    @JsonBackReference("dataset-model") // Matches parent
    private DataSet dataSet;

    // Parent link to Deployments
    @OneToMany(mappedBy = "model")
    @JsonManagedReference("model-deployment") // Unique name
    private List<ModelDeployment> deployments;
}
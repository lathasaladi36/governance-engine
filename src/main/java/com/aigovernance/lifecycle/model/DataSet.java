package com.aigovernance.lifecycle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "dataset")
@Data
@NoArgsConstructor
public class DataSet {

    @Id
    @Column(name = "dataset_id")
    private Integer datasetId;

    @Column(name = "dataset_type")
    private String datasetType;

    @Column(name = "dataset_label")
    private String datasetLabel;

    @Column(name = "dataset_format")
    private String datasetFormat;

    @Column(name = "dataset_size")
    private Double datasetSize;

    @Column(name = "dataset_description")
    private String datasetDescription;

    // FIX: Change Integer to the SourceOfData object
    // This MUST be named sourceOfData to match the mappedBy in SourceOfData.java
    // Parent link to SourceOfData
    @ManyToOne
    @JoinColumn(name = "source_id")
    @JsonBackReference("source-dataset") // Named
    private SourceOfData sourceOfData;

    // Child link to Models
    @OneToMany(mappedBy = "dataSet")
    @JsonManagedReference("dataset-model") // Named
    private List<Model> models;
}
package com.aigovernance.lifecycle.model;

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
    @ManyToOne
    @JoinColumn(name = "source_id")
    private SourceOfData sourceOfData;

    @OneToMany(mappedBy = "dataSet")
    @JsonManagedReference
    private List<Model> models;
}
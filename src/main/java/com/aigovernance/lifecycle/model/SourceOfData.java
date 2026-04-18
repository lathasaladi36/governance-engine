package com.aigovernance.lifecycle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "sourceofdata")
@Data
@NoArgsConstructor
public class SourceOfData {

    @Id
    @Column(name = "source_id")
    private Integer sourceId;

    @Column(name = "datasource_name")
    private String datasourceName;

    @Column(name = "datasource_type")
    private String datasourceType; // Ensure no underscore here for the variable name

    @Column(name = "datasource_details")
    private String datasourceDetails; // Ensure no underscore here

    @Column(name = "datasource_location")
    private String datasourceLocation; // Ensure no underscore here
}
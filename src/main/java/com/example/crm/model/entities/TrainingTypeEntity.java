package com.example.crm.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "TRAINING_TYPE")
public class TrainingTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long typeId;

    @Column(nullable = false)
    private String name;

}

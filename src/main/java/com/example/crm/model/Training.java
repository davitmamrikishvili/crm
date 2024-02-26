package com.example.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRAINING")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainingId;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TrainingType type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer duration;

}

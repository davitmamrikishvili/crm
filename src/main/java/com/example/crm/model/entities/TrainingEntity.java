package com.example.crm.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRAINING")
public class TrainingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainingId;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private TraineeEntity traineeEntity;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private TrainerEntity trainerEntity;

    @Column(nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TrainingTypeEntity type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer duration;

}

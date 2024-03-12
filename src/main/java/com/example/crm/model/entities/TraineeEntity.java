package com.example.crm.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "TRAINEE")
public class TraineeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long traineeId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String address;

    private LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "traineeEntities")
    private Set<TrainerEntity> trainerEntities;

    public TraineeEntity() {
    }

}

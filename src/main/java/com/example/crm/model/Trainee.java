package com.example.crm.model;

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
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long traineeId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    private String address;

    private LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "trainees")
    private Set<Trainer> trainers;

    public Trainee() {
    }

}

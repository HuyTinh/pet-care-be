package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="profiles")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "last_name", nullable = false)
    String last_name;

    @Column(name = "first_name", nullable = false)
    String first_name;

    @Column(name = "email", nullable = true)
    String email;

    @Column(name = "phone_number", nullable = true)
    String phone_number;

    @Column(name = "gender", nullable = true)
    boolean gender = true;

    @Column(name = "address", nullable = true)
    String address;

    @Column(name = "status", nullable = false)
    boolean status = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "profile")
    @JsonIgnore
    Set<Prescription> prescription;
}

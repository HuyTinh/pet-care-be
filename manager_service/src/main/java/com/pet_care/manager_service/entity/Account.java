package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="accounts")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@RedisHash(timeToLive = 1000, value = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "status", nullable = false)
    boolean status;

    @OneToOne(mappedBy = "account")
    Profile profile;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    Set<Customer> customer;
}

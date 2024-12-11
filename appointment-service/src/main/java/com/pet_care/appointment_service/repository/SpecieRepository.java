package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.entity.Specie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecieRepository extends JpaRepository<Specie, String> {
}

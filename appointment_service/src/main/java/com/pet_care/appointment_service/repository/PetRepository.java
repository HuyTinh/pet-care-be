package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByAppointment_Id(Long appointment_id);

    void deleteAllByAppointment_Id(Long appointment_id);
}

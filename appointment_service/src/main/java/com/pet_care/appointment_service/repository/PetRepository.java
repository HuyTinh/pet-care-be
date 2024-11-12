package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Marks this interface as a Spring Data repository to handle database operations for the Pet entity
public interface PetRepository extends JpaRepository<Pet, Long> {

    /**
     * Finds pets by their associated appointment's ID.
     *
     * @param appointment_id The ID of the appointment
     * @return A list of pets associated with the given appointment ID
     */
    List<Pet> findByAppointment_Id(Long appointment_id);

    /**
     * Deletes all pets associated with the specified appointment ID.
     *
     * @param appointment_id The ID of the appointment for which associated pets will be deleted
     */
    void deleteAllByAppointment_Id(Long appointment_id);
}

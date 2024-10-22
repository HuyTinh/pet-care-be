package com.petcare.repository;

import com.petcare.entity.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>
{

    @Query("SELECT u FROM Appointment u " +
            "JOIN u.owner o " +
            "JOIN o.pet p " +
            "JOIN p.prescriptions pres " +
            "JOIN pres.prescriptionDetails detail " +
            "WHERE p.id = :petId")
    List<Appointment> findAppointmentByPetId(@Param("petId") long petId);


//    @EntityGraph(attributePaths = {"owner", "owner.pet", "owner.pet.prescriptions", "owner.pet.prescriptions.prescriptionDetails"})
//    @Query("SELECT u FROM Appointment u " +
//            "JOIN u.owner o " +
//            "JOIN o.pet p " +
//            "JOIN p.prescriptions pres " +
//            "JOIN pres.prescriptionDetails detail " +
//            "WHERE p.id = :petId")
//    List<Appointment> findAppointmentByPetId(long petId);


}

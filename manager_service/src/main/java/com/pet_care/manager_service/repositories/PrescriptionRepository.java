package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query(value = "SELECT pr.id, pr.create_date, pr.note, pr.disease_name " +
            " FROM Prescription pr " +
            " JOIN pr.pet p " +
            " WHERE p.id = :id ")
    List<Object[]> getPrescriptionByPetId(
            @Param("id") Long id
    );

    @Query(value = "SELECT pr.id, pr.create_date " +
            "     , cts.id, pe.id, sp.id, pf.id " +
            "    FROM prescriptions pr " +
            "        JOIN profiles pf on pr.profile_id = pf.id " +
            "        JOIN pets pe on pr.pet_id = pe.id " +
            "        JOIN species sp on pe.species_id = sp.id " +
            "        JOIN customers cts on pe.customer_id = cts.id " +
            "    WHERE pr.create_date = :date AND pr.status = true ", nativeQuery = true)
    List<Object[]> getPrescriptionToday(@Param("date")LocalDate date);
}

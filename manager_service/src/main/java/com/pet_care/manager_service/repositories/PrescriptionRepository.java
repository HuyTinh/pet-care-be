package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query(value = "SELECT pr.id, pr.create_date, pr.note" +
            " FROM Prescription pr " +
            " JOIN pr.pet p " +
            " WHERE p.id = :id ")
    Object[] getPrescriptionByPetId(
            @Param("id") Long id
    );
}

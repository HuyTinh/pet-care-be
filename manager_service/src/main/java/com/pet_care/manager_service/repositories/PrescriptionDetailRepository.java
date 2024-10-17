package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Prescription;
import com.pet_care.manager_service.entity.Prescription_Details;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionDetailRepository extends JpaRepository<Prescription_Details, Long> {

    List<Prescription_Details> findByPrescription(Prescription prescription);
}

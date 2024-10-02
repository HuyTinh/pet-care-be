package com.pet_care.medicine_service.repository;

import com.pet_care.medicine_service.model.CalculationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationUnitRepository extends JpaRepository<CalculationUnit, Long> { }

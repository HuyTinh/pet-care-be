package com.pet_care.medicine_service.repository;

import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {


}

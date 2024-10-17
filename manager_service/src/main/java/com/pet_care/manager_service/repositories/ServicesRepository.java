package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    Optional<Services> findServicesByName(String name);

    @Query(value = "SELECT s.name as service_name " +
            " FROM Customer  ctm " +
            " JOIN ctm.pet p " +
            " JOIN p.species sc " +
            " JOIN p.prescription prs " +
            " JOIN ctm.appointments ap " +
            " JOIN ap.appointment_service aps " +
            " JOIN aps.services s " +
            " WHERE ctm.id = :id ")
    List<Object[]> findServicesByCustomerId(@Param("id") Long id);
}

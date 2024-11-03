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
            "             FROM customers ctm " +
            "             JOIN pets p on ctm.id = p.customer_id " +
            "             JOIN species sc on p.species_id = sc.id " +
            "             JOIN prescriptions prs on p.id = prs.pet_id " +
            "             JOIN appointments ap on ctm.id = ap.customer_id " +
            "             JOIN appointment_services aps on ap.id = aps.appointment_id " +
            "             JOIN services s on aps.services_id = s.id " +
            "             WHERE ctm.id = :id " +
            "             GROUP BY s.name " , nativeQuery = true)
    List<Object[]> findServicesByCustomerId(@Param("id") Long id);

    @Query(value = "SELECT sv FROM Services sv WHERE sv.status = true ")
    List<Services> getAllServices();
}

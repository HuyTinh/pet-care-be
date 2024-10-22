package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, " +
            "             p.name as pet_name, p.weight, p.age, sc.name as species_name, s.name as service_name " +
            "            FROM customers ctm " +
            "            JOIN pets p on ctm.id = p.customer_id " +
            "            JOIN species sc on p.species_id = sc.id " +
            "            JOIN prescriptions prs on p.id = prs.pet_id " +
            "            JOIN appointments ap on ctm.id = ap.customer_id " +
            "            JOIN appointment_services aps  on ap.id = aps.appointment_id " +
            "            JOIN services s on aps.services_id = s.id "
            , nativeQuery = true )
    List<Object[]> getAllCustomer();

    @Query(value = "SELECT ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, " +
            "             p.name as pet_name, p.weight, p.age, sc.name as species_name, s.name as service_name " +
            "            FROM customers ctm " +
            "            JOIN pets p on ctm.id = p.customer_id " +
            "            JOIN species sc on p.species_id = sc.id " +
            "            JOIN prescriptions prs on p.id = prs.pet_id " +
            "            JOIN appointments ap on ctm.id = ap.customer_id " +
            "            JOIN appointment_services aps  on ap.id = aps.appointment_id " +
            "            JOIN services s on aps.services_id = s.id" +
            "            WHERE ctm.status = true ", nativeQuery = true )
    List<Object[]> getAllCustomerByStatusTrue();

}

package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, " +
            " p.name as pet_name, p.weight, p.age, sc.name as species_name, s.name as service_name" +
            " FROM Customer  ctm " +
            " JOIN ctm.pet p " +
            " JOIN p.species sc " +
            " JOIN p.prescription prs " +
            " JOIN ctm.appointments ap " +
            " JOIN ap.appointment_service aps " +
            " JOIN aps.services s " )
    List<Object[]> getAllCustomer();

    @Query(value = "SELECT ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, " +
            " p.name as pet_name, p.weight, p.age, sc.name as species_name, s.name as service_name" +
            " FROM Customer  ctm " +
            " JOIN ctm.pet p " +
            " JOIN p.species sc " +
            " JOIN p.prescription prs " +
            " JOIN ctm.appointments ap " +
            " JOIN ap.appointment_service aps " +
            " JOIN aps.services s " +
            " WHERE ctm.status = true" )
    List<Object[]> getAllCustomerByStatusTrue();
}

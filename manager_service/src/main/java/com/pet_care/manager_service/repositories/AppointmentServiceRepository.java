package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Appointment_Service;
import org.springframework.data.repository.CrudRepository;

public interface AppointmentServiceRepository extends CrudRepository<Appointment_Service, Long> {
}

package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT app FROM Appointment app " +
            " WHERE app.appointment_date = :date and app.status = true ORDER BY app.id DESC ")
    List<Appointment> getAllAppointmentToDay(@Param("date") LocalDate date);
}

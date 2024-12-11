package com.pet_care.notification_service.enums;

// Enum representing possible statuses for an appointment
public enum AppointmentStatus {
    SCHEDULED,      // Appointment has been scheduled
    PENDING,        // Appointment is pending confirmation
    CHECKED_IN,     // Appointment has been checked-in
    CANCELLED,      // Appointment has been cancelled
    COMPLETED,      // Appointment has been completed
    APPROVED,       // Appointment has been approved
    NO_SHOW         // Appointment was missed (no-show)
}


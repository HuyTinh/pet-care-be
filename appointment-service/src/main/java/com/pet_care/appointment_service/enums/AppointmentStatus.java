package com.pet_care.appointment_service.enums;

/**
 * Enum representing the different statuses of an appointment.
 */
public enum AppointmentStatus {

    // The appointment is scheduled but not yet confirmed or attended.
    SCHEDULED,

    // The appointment has been checked-in by the customer or attendee.
    CHECKED_IN,

    // The appointment has been canceled and will not occur.
    CANCELLED,

    APPROVED,

    // The appointment has been completed successfully.
    COMPLETED,

    // The customer or attendee did not show up for the appointment.
    NO_SHOW
}

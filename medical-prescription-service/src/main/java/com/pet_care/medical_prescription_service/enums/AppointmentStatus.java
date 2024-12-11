package com.pet_care.medical_prescription_service.enums;

public enum AppointmentStatus {
    SCHEDULED,      // Đã lên lịch
    PENDING,        // Đang chờ xác nhận
    CHECKED_IN,     // Đã check-in
    CANCELLED,      // Đã hủy
    COMPLETED,
    APPROVED,// Đã hoàn thành
    NO_SHOW
}
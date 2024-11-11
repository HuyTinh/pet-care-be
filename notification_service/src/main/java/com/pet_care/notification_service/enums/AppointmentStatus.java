package com.pet_care.notification_service.enums;

public enum AppointmentStatus {
    SCHEDULED,      // Đã lên lịch
    PENDING,        // Đang chờ xác nhận
    CHECKED_IN,     // Đã check-in
    CANCELLED,      // Đã hủy
    COMPLETED,
    APPROVED,// Đã hoàn thành
    NO_SHOW
}

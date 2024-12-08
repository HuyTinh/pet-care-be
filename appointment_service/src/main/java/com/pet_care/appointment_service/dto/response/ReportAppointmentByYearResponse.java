package com.pet_care.appointment_service.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public interface ReportAppointmentByYearResponse {
    String getMonth();
    @JsonProperty("total_appointment")
    Integer getTotalAppointments();
    @JsonProperty("number_of_scheduled")
    Integer getNumberOfScheduled();
    @JsonProperty("number_of_approved")
    Integer getNumberOfApproved();
    @JsonProperty("number_of_cancelled")
    Integer getNumberOfCancelled();
    @JsonProperty("number_of_no_show")
    Integer getNumberOfNoShow();
 }

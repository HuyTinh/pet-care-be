package com.pet_care.appointment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.exception.AppointmentException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.mapper.AppointmentMapper;
import com.pet_care.appointment_service.mapper.PetMapper;
import com.pet_care.appointment_service.model.Appointment;
import com.pet_care.appointment_service.model.AppointmentBookingSuccessful;
import com.pet_care.appointment_service.model.Pet;
import com.pet_care.appointment_service.repository.AppointmentRepository;
import com.pet_care.appointment_service.repository.HospitalServiceRepository;
import com.pet_care.appointment_service.repository.PetRepository;
import com.pet_care.appointment_service.utils.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {

    AppointmentRepository appointmentRepository;

    HospitalServiceRepository hospitalServiceRepository;

    AppointmentMapper appointmentMapper;

    MessageService messageService;

    Queue<String> queue;

    ObjectMapper objectMapper;

    PetRepository petRepository;

    PetMapper petMapper;

//    public AppointmentResponse createNoneEmailNotification(AppointmentCreateRequest appointmentCreateRequest) throws JsonProcessingException {
//        return createAppointment(appointmentCreateRequest, false);
//    }
//
//    public AppointmentResponse createWithEmailNotification(AppointmentCreateRequest appointmentCreateRequest) throws JsonProcessingException {
//        return createAppointment(appointmentCreateRequest, true);
//    }

    public AppointmentResponse updateAppointment(Long appointmentId, AppointmentUpdateRequest appointmentUpdateRequest) throws JsonProcessingException {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));

        appointmentMapper.partialUpdate(appointmentUpdateRequest, existingAppointment);

        petRepository.deleteAllById(appointmentUpdateRequest.getPets().stream()
                .map(petCreateRequest -> petCreateRequest.getId() == null ?
                        0 : petCreateRequest.getId()).toList());


        petRepository.saveAll(appointmentUpdateRequest.getPets().stream()
                .peek(petCreateRequest -> petCreateRequest
                        .setAppointment(existingAppointment))
                .collect(Collectors.toSet()));

        Appointment updateAppointment = appointmentRepository.save(existingAppointment);

        log.info("Appointment Service: Update appointment successful");

        return appointmentMapper.toDto(updateAppointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointment() {
        List<AppointmentResponse> appointmentResponses = appointmentRepository.findAll().stream().map(appointment -> {
            AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
            appointmentResponse.setPets(petRepository
                    .findByAppointment_Id(appointment.getId()).stream()
                    .map(petMapper::toDto)
                    .collect(Collectors.toSet()));
            return appointmentResponse;
        }).toList();

        log.info("Appointment Service: Get all appointments successful");

        return appointmentResponses;
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment existingAppointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));

        AppointmentResponse appointmentResponse = appointmentMapper
                .toDto(existingAppointment);

        appointmentResponse.setPets(petRepository
                .findByAppointment_Id(existingAppointment.getId()).stream()
                .map(petMapper::toDto)
                .collect(Collectors.toSet()));

        log.info("Appointment Service: Get appointment by id successful");

        return appointmentResponse;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointmentByAccountId(Long accountId) {

        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAllByAccountId(accountId).stream().map(appointment -> {
                    AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
                    appointmentResponse.setPets(new HashSet<>(petRepository
                            .findByAppointment_Id(appointment.getId())).stream()
                            .map(petMapper::toDto)
                            .collect(Collectors.toSet()));
                    return appointmentResponse;
                }).collect(Collectors.toList());

        log.info("Appointment Service: Get appointment by account id successful");

        return appointmentResponses;
    }

    public int checkInAppointment(Long appointmentId) {
        int checkIn = appointmentRepository
                .checkInAppointment(appointmentId);

        log.info("Appointment Service: Check in appointment successful");

        return checkIn;
    }

    public int cancelAppointment(Long appointmentId) {
        int cancel = appointmentRepository
                .cancelledAppointment(appointmentId);

        log.info("Appointment Service: Cancel appointment successful");

        return cancel;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByStatus(String status) {
        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAppointmentByStatus(AppointmentStatus
                        .valueOf(status)).stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());

        log.info("Appointment Service: Get appointment by status successful");

        return appointmentResponses;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByStatusAndAccountId(String status, Long accountId) {
        try {
            Sort sort = Sort.by("appointmentDate").ascending();
            return appointmentRepository
                    .findAppointmentByStatusAndAccountId(AppointmentStatus
                            .valueOf(status), accountId, sort)
                    .stream()
                    .map(appointment -> {
                        AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
                        appointmentResponse
                                .setPets(new HashSet<>(petRepository
                                        .findByAppointment_Id(appointment.getId()))
                                        .stream().map(petMapper::toDto)
                                        .collect(Collectors.toSet()));
                        return appointmentResponse;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new AppointmentException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointmentByAppointmentDateAndAndStatusIn(Date appointmentDate, Set<AppointmentStatus> statuses) {
        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAppointmentByAppointmentDateAndStatusIn(appointmentDate, statuses).stream()
                .map(appointment -> {
                    AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
                    appointmentResponse.setPets(new HashSet<>(petRepository
                            .findByAppointment_Id(appointment.getId())).stream()
                            .map(petMapper::toDto)
                            .collect(Collectors.toSet()));
                    return appointmentResponse;
                }).collect(Collectors.toList());

        log.info("Appointment Service: Get appointment by appointment date and status in successful");

        return appointmentResponses;
    }

//    @JmsListener(destination = "customer-create-appointment-queue", containerFactory = "queueFactory")
//    public void receiveMessageBrokerCustomerCreateAppointment(String message) {
//        try {
//            AppointmentCreateRequest appointmentCreateRequest = objectMapper.readValue(message, AppointmentCreateRequest.class);
//            this.createNoneEmailNotification(appointmentCreateRequest);
//            log.info("Appointment Service: Customer createAppointment appointment with NONE notification successful");
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @JmsListener(destination = "customer-create-appointment-with-notification-queue", containerFactory = "queueFactory")
//    public void receiveMessageBrokerCustomerCreateAppointmentWithEmailNotification(String message) {
//        try {
//
//            AppointmentCreateRequest appointmentCreateRequest = objectMapper.readValue(message, AppointmentCreateRequest.class);
//            this.createWithEmailNotification(appointmentCreateRequest);
//            log.info("Appointment Service: Customer createAppointment appointment with notification successful");
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    public AppointmentResponse createAppointment(AppointmentCreateRequest appointmentCreateRequest, Boolean notification) throws JsonProcessingException {
        Appointment appointment = appointmentMapper.toEntity(appointmentCreateRequest);

        appointment.setServices(new HashSet<>(hospitalServiceRepository
                .findAllById(appointmentCreateRequest.getServices())));

        if (appointmentCreateRequest.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.PENDING);
        }

        Appointment createSuccess = appointmentRepository.save(appointment);

        Set<Pet> pets = appointmentCreateRequest.getPets().stream()
                .peek(pet -> pet.setAppointment(createSuccess))
                .collect(Collectors.toSet());

        petRepository.saveAll(pets);

        String createAppointmentStatus = createSuccess.getStatus().name();
        AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);

        if (createAppointmentStatus.equals("CHECKED_IN")) {
            messageService.sendMessage("doctor-appointment-queue", objectMapper.writeValueAsString(appointmentResponse));
        } else {
            try {
                queue.add(objectMapper.writeValueAsString(appointmentResponse));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (notification) {
            AppointmentBookingSuccessful appointmentBookingSuccessful = AppointmentBookingSuccessful.builder()
                    .appointmentId(appointmentResponse.getId())
                    .appointmentDate(DateUtil.getDateOnly(appointmentResponse.getAppointmentDate()))
                    .appointmentTime(DateUtil.getTimeOnly(appointmentResponse.getAppointmentTime()))
                    .toEmail(appointmentResponse.getEmail())
                    .firstName(appointmentResponse.getFirstName())
                    .lastName(appointmentResponse.getLastName())
                    .build();


            messageService.sendMessage("appointment-success-notification-queue", appointmentBookingSuccessful.getContent());
        }

        return appointmentMapper.toDto(createSuccess);
    }
}

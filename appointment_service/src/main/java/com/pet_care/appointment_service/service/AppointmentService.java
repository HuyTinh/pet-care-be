package com.pet_care.appointment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.mapper.AppointmentMapper;
import com.pet_care.appointment_service.mapper.HospitalServiceMapper;
import com.pet_care.appointment_service.mapper.PetMapper;
import com.pet_care.appointment_service.model.Appointment;
import com.pet_care.appointment_service.model.EmailBookingSuccessful;
import com.pet_care.appointment_service.model.HospitalServiceEntity;
import com.pet_care.appointment_service.model.Pet;
import com.pet_care.appointment_service.repository.AppointmentRepository;
import com.pet_care.appointment_service.repository.HospitalServiceRepository;
import com.pet_care.appointment_service.repository.PetRepository;
import com.pet_care.appointment_service.utils.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {
    @NotNull
    HospitalServiceMapper hospitalServiceMapper;

    @NotNull AppointmentRepository appointmentRepository;

    @NotNull HospitalServiceRepository hospitalServiceRepository;

    @NotNull AppointmentMapper appointmentMapper;

    @NotNull MessageService messageService;

    @NotNull Queue<String> queue;

    @NotNull ObjectMapper objectMapper;

    @NotNull PetRepository petRepository;

    @NotNull PetMapper petMapper;

    /**
     * @param appointmentId
     * @param appointmentUpdateRequest
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    public AppointmentResponse updateAppointment(@NotNull Long appointmentId, @NotNull AppointmentUpdateRequest appointmentUpdateRequest) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        appointmentMapper.partialUpdate(appointmentUpdateRequest, existingAppointment);

        petRepository.deleteAllByAppointment_Id(appointmentId);

        petRepository.saveAll(appointmentUpdateRequest.getPets().stream()
                .peek(petCreateRequest -> petCreateRequest
                        .setAppointment(existingAppointment))
                .collect(toSet()));

        existingAppointment.setServices(new HashSet<>(hospitalServiceRepository.findAllById(appointmentUpdateRequest.getServices())));

        Appointment updateAppointment = appointmentRepository.save(existingAppointment);

        log.info("Appointment Service: Update appointment successful");

        return appointmentMapper.toDto(updateAppointment);
    }

    /**
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointment() {
        List<AppointmentResponse> appointmentResponses = appointmentRepository.findAll().stream().map(appointment -> {
            AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
            appointmentResponse.setPets(petRepository
                    .findByAppointment_Id(appointment.getId()).stream()
                    .map(petMapper::toDto)
                    .collect(toSet()));
            return appointmentResponse;
        }).toList();

        log.info("Appointment Service: Get all appointments successful");

        return appointmentResponses;
    }

    /**
     * @param appointmentId
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(@NotNull Long appointmentId) {
        Appointment existingAppointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        AppointmentResponse appointmentResponse = appointmentMapper
                .toDto(existingAppointment);

        appointmentResponse.setPets(petRepository
                .findByAppointment_Id(existingAppointment.getId()).stream()
                .map(petMapper::toDto)
                .collect(toSet()));

        log.info("Appointment Service: Get appointment by id successful");

        return appointmentResponse;
    }

    /**
     * @param accountId
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointmentByAccountId(Long accountId) {

        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAllByAccountId(accountId).stream().map(appointment -> {
                    AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
                    appointmentResponse.setPets(new HashSet<>(petRepository
                            .findByAppointment_Id(appointment.getId())).stream()
                            .map(petMapper::toDto)
                            .collect(toSet()));
                    return appointmentResponse;
                }).collect(toList());

        log.info("Appointment Service: Get appointment by account id successful");

        return appointmentResponses;
    }

    /**
     * @param appointmentId
     * @return
     */
    public int checkInAppointment(Long appointmentId) {
        System.out.println(appointmentId);
        int checkIn = appointmentRepository
                .checkInAppointment(appointmentId);

        log.info("Appointment Service: Check in appointment successful");

        return checkIn;
    }

    /**
     * @param appointmentId
     * @return
     */
    public int cancelAppointment(Long appointmentId) {
        int cancel = appointmentRepository
                .cancelledAppointment(appointmentId);

        log.info("Appointment Service: Cancel appointment successful");

        return cancel;
    }

    /**
     * @param status
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByStatus(String status) {
        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAppointmentByStatus(AppointmentStatus
                        .valueOf(status)).stream()
                .map(appointment -> {
                    AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
                    appointmentResponse.setServices(appointment.getServices().stream().map(HospitalServiceEntity::getName).collect(toSet()));
                    appointmentResponse
                            .setPets(new HashSet<>(petRepository
                                    .findByAppointment_Id(appointment.getId()))
                                    .stream().map(petMapper::toDto)
                                    .collect(toSet()));
                    return appointmentResponse;
                })
                .collect(toList());

        log.info("Appointment Service: Get appointment by status successful");

        return appointmentResponses;
    }

    /**
     * @param status
     * @param accountId
     * @return
     */
    @NotNull
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

                        appointmentResponse.setServices(appointment.getServices().stream().map(HospitalServiceEntity::getName).collect(toSet()));

                        appointmentResponse
                                .setPets(new HashSet<>(petRepository
                                        .findByAppointment_Id(appointment.getId()))
                                        .stream().map(petMapper::toDto)
                                        .collect(toSet()));
                        return appointmentResponse;
                    }).collect(toList());
        } catch (Exception e) {
            throw new APIException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
    }

    /**
     * @param appointmentDate
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointmentByAppointmentDate(Date appointmentDate) {
        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAppointmentByAppointmentDate(appointmentDate).stream()
                .map(appointment -> {
                    AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);
                    appointmentResponse.setPets(new HashSet<>(petRepository
                            .findByAppointment_Id(appointment.getId())).stream()
                            .map(petMapper::toDto)
                            .collect(toSet()));
                    return appointmentResponse;
                }).collect(toList());

        log.info("Appointment Service: Get appointment by appointment date and status in successful");

        return appointmentResponses;
    }

    /**
     * @param startDate
     * @param endDate
     * @param statues
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<AppointmentResponse> filterAppointments(@NotNull LocalDate startDate, @NotNull LocalDate endDate, @Nullable Set<String> statues) {

        Date sDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date eDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        List<Appointment> appointmentsBetweenDate = appointmentRepository.findByAppointmentDateBetween(sDate, eDate);

        List<AppointmentResponse> appointmentResponses = appointmentsBetweenDate.stream().map(appointmentMapper::toDto).toList();




        if (statues != null) {
            appointmentResponses = appointmentsBetweenDate.stream()
                    .map(appointmentMapper::toDto)
                    .filter(appointment -> statues.stream()
                            .anyMatch(s -> s.equals(appointment.getStatus().name()))).toList();
        }


        appointmentResponses = appointmentResponses.stream().peek(appointmentResponse -> appointmentResponse.setPets(new HashSet<>(petRepository
                .findByAppointment_Id(appointmentResponse.getId())).stream()
                .map(petMapper::toDto)
                .collect(toSet()))).toList();


        System.out.println(appointmentResponses);

        log.info("Appointment Service: Filter appointments successful");

        return appointmentResponses;
    }

    /**
     * @param appointmentCreateRequest
     * @param notification
     * @return
     * @throws JsonProcessingException
     */
    @NotNull
    @Transactional
    public AppointmentResponse createAppointment(@NotNull AppointmentCreateRequest appointmentCreateRequest, Boolean notification) throws JsonProcessingException {
        Appointment appointment = appointmentMapper.toEntity(appointmentCreateRequest);

        Set<HospitalServiceEntity> bookingService = new HashSet<>(hospitalServiceRepository
                .findAllById(appointmentCreateRequest.getServices()));

        appointment.setServices(bookingService);

        if (appointmentCreateRequest.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.PENDING);
        }

        Appointment createSuccess = appointmentRepository.save(appointment);

        Set<Pet> pets = appointmentCreateRequest.getPets().stream()
                .map(petMapper::toEntity)
                .peek(pet -> pet.setAppointment(createSuccess))
                .collect(toSet());

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
            EmailBookingSuccessful emailBookingSuccessful = EmailBookingSuccessful.builder()
                    .appointmentId(appointmentResponse.getId())
                    .appointmentDate(DateUtil.getDateOnly(appointmentResponse.getAppointmentDate()))
                    .appointmentTime(DateUtil.getTimeOnly(appointmentResponse.getAppointmentTime()))
                    .toEmail(appointmentResponse.getEmail())
                    .firstName(appointmentResponse.getFirstName())
                    .lastName(appointmentResponse.getLastName())
                    .build();


            messageService.sendMessage("appointment-success-notification-queue", emailBookingSuccessful.getContent());
        }

        appointmentResponse.setPets(pets.stream().map(petMapper::toDto).collect(toSet()));
        appointmentResponse.setServices(bookingService.stream().map(HospitalServiceEntity::getName).collect(toSet()));

        if (appointmentCreateRequest.getAccountId() == null) {
            appointmentResponse.setAccount("GUEST");
        }

        return appointmentResponse;
    }
}

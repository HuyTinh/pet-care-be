package com.pet_care.appointment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.dto.response.PageableResponse;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.mapper.AppointmentMapper;
import com.pet_care.appointment_service.mapper.HospitalServiceMapper;
import com.pet_care.appointment_service.mapper.PetMapper;
import com.pet_care.appointment_service.model.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {
    
    HospitalServiceMapper hospitalServiceMapper;

     AppointmentRepository appointmentRepository;

     HospitalServiceRepository hospitalServiceRepository;

     AppointmentMapper appointmentMapper;

     MessageService messageService;

     Queue<String> queue;

     ObjectMapper objectMapper;

     PetRepository petRepository;

     PetMapper petMapper;

    /**
     * @param appointmentId
     * @param appointmentUpdateRequest
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    public AppointmentResponse updateAppointment( Long appointmentId,  AppointmentUpdateRequest appointmentUpdateRequest) {
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
    
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById( Long appointmentId) {
        Appointment existingAppointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        AppointmentResponse appointmentResponse = appointmentMapper
                .toDto(existingAppointment);

        appointmentResponse.setPets(petRepository
                .findByAppointment_Id(existingAppointment.getId()).stream()
                .map(petMapper::toDto)
                .collect(toSet()));

        appointmentResponse.setServices(existingAppointment.getServices().stream().map(hospitalServiceMapper::toDto).collect(toSet()));

        log.info("Appointment Service: Get appointment by id successful");

        return appointmentResponse;
    }

    /**
     * @param accountId
     * @return
     */
    
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
     * @param appointmentId
     * @return
     */
    public int approvedAppointment(Long appointmentId) {
        int cancel = appointmentRepository
                .approvedAppointment(appointmentId);

        log.info("Appointment Service: Approved appointment successful");

        return cancel;
    }

    /**
     * @param status
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByStatus(String status) {
        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAppointmentByStatus(AppointmentStatus
                        .valueOf(status)).stream()
                .map(this::toAppointmentResponse)
                .collect(toList());

        log.info("Appointment Service: Get appointment by status successful");

        return appointmentResponses;
    }

    /**
     * @param status
     * @param accountId
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByStatusAndAccountId(String status, Long accountId) {
        try {
            Sort sort = Sort.by("appointmentDate").ascending();
            return appointmentRepository
                    .findAppointmentByStatusAndAccountId(AppointmentStatus
                            .valueOf(status), accountId, sort)
                    .stream()
                    .map(this::toAppointmentResponse).collect(toList());
        } catch (Exception e) {
            throw new APIException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
    }

    /**
     * @param appointmentDate
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointmentByAppointmentDate(Date appointmentDate) {
        List<AppointmentResponse> appointmentResponses = appointmentRepository
                .findAppointmentByAppointmentDate(appointmentDate).stream()
                .map(this::toAppointmentResponse).collect(toList());

        log.info("Appointment Service: Get appointment by appointment date and status in successful");

        return appointmentResponses;
    }

    /**
     * @param startDate
     * @param endDate
     * @param statues
     * @return
     */
    
    @Transactional(readOnly = true)
    public PageableResponse<AppointmentResponse> filterAppointments(int page, int size,  LocalDate startDate,  LocalDate endDate, @Nullable Set<String> statues) {

        Date sDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Date eDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());

        CompletableFuture<Page<Appointment>> appointmentsBetweenDateFuture = CompletableFuture.supplyAsync(() -> appointmentRepository.findByAppointmentDateBetweenAndStatusIn(sDate, eDate, Objects.requireNonNullElse(statues, new HashSet<>()),pageable));

        Page<AppointmentResponse> appointmentResponses = appointmentsBetweenDateFuture.thenApply(appointments -> appointments.map(this::toAppointmentResponse)).join();

        return PageableResponse.<AppointmentResponse>builder()
                .content(appointmentResponses.getContent())
                .pageNumber(appointmentResponses.getPageable().getPageNumber())
                .pageSize(appointmentResponses.getPageable().getPageSize())
                .totalPages(appointmentResponses.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public PageableResponse<AppointmentResponse> getAllAppointmentByAccountIdAndStatuesAndBetween(int page, int size,  LocalDate startDate,  LocalDate endDate, @Nullable Set<String> statues, Long accountId) {


        Date sDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Date eDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        CompletableFuture<Page<Appointment>> appointmentsBetweenDateFuture = CompletableFuture.supplyAsync(() -> appointmentRepository.findAppointmentByAccountIdAndStatusInAndAppointmentDateBetween(accountId, statues, sDate, eDate ,pageable));

        Page<AppointmentResponse> appointmentResponses = appointmentsBetweenDateFuture.thenApply(appointments -> appointments.map(this::toAppointmentResponse)).join();

        return PageableResponse.<AppointmentResponse>builder()
                .content(appointmentResponses.getContent())
                .pageNumber(appointmentResponses.getPageable().getPageNumber())
                .pageSize(appointmentResponses.getPageable().getPageSize())
                .totalPages(appointmentResponses.getTotalPages())
                .build();
    }

    @Transactional(readOnly = true)
    public PageableResponse<AppointmentResponse> getAllAppointmentByAccountIdAndStatues(int page, int size, Long accountId,Set<String> statues) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        CompletableFuture<Page<Appointment>> appointmentsBetweenDateFuture = CompletableFuture.supplyAsync(() -> appointmentRepository.findByAccountIdAndStatusIn(accountId, Objects.requireNonNullElse(statues, new HashSet<>()),pageable));

        Page<AppointmentResponse> appointmentResponses = appointmentsBetweenDateFuture.thenApply(appointments -> appointments.map(this::toAppointmentResponse)).join();

        return PageableResponse.<AppointmentResponse>builder()
                .content(appointmentResponses.getContent())
                .pageNumber(appointmentResponses.getPageable().getPageNumber())
                .pageSize(appointmentResponses.getPageable().getPageSize())
                .totalPages(appointmentResponses.getTotalPages())
                .build();
    }

    /**
     * @param appointmentCreateRequest
     * @param notification
     * @return
     * @throws JsonProcessingException
     */
    
    @Transactional
    public AppointmentResponse createAppointment( AppointmentCreateRequest appointmentCreateRequest, Boolean notification) throws JsonProcessingException {
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
            if(appointmentResponse.getAppointmentDate().equals(new Date())) {
                try {
                    queue.add(objectMapper.writeValueAsString(appointmentResponse));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
        appointmentResponse.setServices(bookingService.stream().map(hospitalServiceMapper::toDto).collect(toSet()));

        if (appointmentCreateRequest.getAccountId() == null) {
            appointmentResponse.setAccount("GUEST");
        }

        return appointmentResponse;
    }

    /**
     * @param appointmentId
     * @param services
     * @return
     * @throws JsonProcessingException
     */
    @Transactional
    public AppointmentResponse updateAppointmentServices(Long appointmentId, Set<String> services) throws JsonProcessingException {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        CompletableFuture<List<HospitalServiceEntity>> hospitalServiceEntityCompletableFuture = CompletableFuture.supplyAsync(() -> hospitalServiceRepository.findAllById(services));


        return hospitalServiceEntityCompletableFuture.thenApply(hospitalServiceEntities -> {
            appointment.setServices(new HashSet<>(hospitalServiceEntities));

            AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointmentRepository.save(appointment));

            appointmentResponse.setPets(petRepository.findByAppointment_Id(appointmentId).stream().map(petMapper::toDto).collect(toSet()));

            appointmentResponse.setServices(appointment.getServices().stream().map(hospitalServiceMapper::toDto).collect(toSet()));

            return appointmentResponse;
        }).join();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointmentUpComing() {
        return appointmentRepository
                .getByAppointmentDateBetweenAndStatus(new Date(), DateUtil.plusDate(new Date(), 3), AppointmentStatus.SCHEDULED).stream().map(appointmentMapper::toDto).collect(toList());
    }

    /**
     * @param appointment
     * @return
     */
    private AppointmentResponse toAppointmentResponse(Appointment appointment) {
        AppointmentResponse appointmentResponse = appointmentMapper.toDto(appointment);

        appointmentResponse.setServices(appointment.getServices().parallelStream().map(hospitalServiceMapper::toDto).collect(toSet()));

        appointmentResponse
                .setPets(new HashSet<>(petRepository
                        .findByAppointment_Id(appointment.getId()))
                        .parallelStream().map(petMapper::toDto)
                        .collect(toSet()));
        return appointmentResponse;
    }
}

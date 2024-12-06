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
import com.pet_care.appointment_service.mapper.PetMapper;
import com.pet_care.appointment_service.entity.*;
import com.pet_care.appointment_service.repository.AppointmentRepository;
import com.pet_care.appointment_service.repository.PetRepository;
import com.pet_care.appointment_service.utils.DateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.pet_care.appointment_service.utils.DateUtil.getDateOnly;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {
     PetRepository petRepository;

     AppointmentRepository appointmentRepository;

     MessageBrokerService messageBrokerService;

     RedisNativeService redisNativeService;

     Queue<String> queue;

     ObjectMapper objectMapper;

     AppointmentMapper appointmentMapper;

     PetMapper petMapper;

    /**
     * @param appointmentId
     * @param appointmentUpdateRequest
     * @return
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

        Appointment updateAppointment = appointmentRepository.save(existingAppointment);

        log.info("Appointment Service: Update appointment successful");

        cacheAppointment();

        return appointmentMapper.toDto(updateAppointment);
    }

    /**
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointment() {
        List<AppointmentResponse> cacheAppointmentResponses = redisNativeService.getRedisList("appointment-response-list", AppointmentResponse.class);

        if(!cacheAppointmentResponses.isEmpty()) {
            return cacheAppointmentResponses;
        } else {
            cacheAppointment();
        }

        List<AppointmentResponse> appointmentResponses = appointmentRepository.findAll().stream().map(this::toAppointmentResponse).toList();

        log.info("Appointment Service: Get all appointments successful");

        return appointmentResponses;
    }

    /**
     * @param appointmentId
     * @return
     */
    
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById( Long appointmentId) {

        List<AppointmentResponse> cacheAppointmentResponses = redisNativeService.getRedisList("appointment-response-list", AppointmentResponse.class);

        if(!cacheAppointmentResponses.isEmpty()) {
            return cacheAppointmentResponses.stream().filter(appointmentResponse -> Objects.equals(appointmentResponse.getId(), appointmentId)).findFirst()
                    .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));
        } else {
            cacheAppointment();
        }

        Appointment existingAppointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new APIException(ErrorCode.APPOINTMENT_NOT_FOUND));

        return toAppointmentResponse(existingAppointment);
    }

    /**
     * @param appointmentId
     * @return
     */
    public int checkInAppointment(Long appointmentId) {
        int checkIn = appointmentRepository
                .checkInAppointment(appointmentId);

        if(checkIn > 0) {
            cacheAppointment();
        }

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

        if(cancel > 0) {
            cacheAppointment();
        }

        log.info("Appointment Service: Cancel appointment successful");

        return cancel;
    }

    /**
     * @param appointmentId
     * @return
     */
    public int approvedAppointment(Long appointmentId) {
        int approved = appointmentRepository
                .approvedAppointment(appointmentId);

        if(approved > 0) {
            cacheAppointment();
        }

        log.info("Appointment Service: Approved appointment successful");

        return approved;
    }

    /**
     * @param status
     * @param accountId
     * @return
     */
    
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getByStatusAndAccountId(String status, Long accountId) {

        try {
            Sort sort = Sort.by("appointmentDate").descending();
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
     * @param startDate
     * @param endDate
     * @param statues
     * @return
     */

    @Transactional(readOnly = true)
    public PageableResponse<AppointmentResponse> filterAppointments(int page, int size,  LocalDate startDate,  LocalDate endDate, Set<String> statues, Long accountId) {

        Date sDate;

        Date eDate;

        List<AppointmentResponse> appointmentCache = redisNativeService.getRedisList("appointment-response-list", AppointmentResponse.class);

        if(startDate != null && endDate != null) {
            sDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            eDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else {
            sDate = null;
            eDate = null;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());

        if(!appointmentCache.isEmpty()) {
            var contentList = appointmentCache;

            if(startDate != null && endDate != null) {
                contentList = contentList.stream()
                        .filter(appointment -> !appointment.getAppointmentDate().before(sDate) && !appointment.getAppointmentDate().after(eDate)).toList();
            }

            appointmentCache = contentList;
        } else {
            cacheAppointment();

            CompletableFuture<List<Appointment>> appointmentsBetweenDateFuture = CompletableFuture.supplyAsync(() -> appointmentRepository.findByAppointmentDateBetween(sDate, eDate));

            appointmentCache = appointmentsBetweenDateFuture.thenApply(appointments -> appointments.stream().map(this::toAppointmentResponse).toList()).join();
        }

        if(accountId != null) {
            appointmentCache = appointmentCache.stream().filter(appointmentResponse -> {
                return Objects.equals(accountId, appointmentResponse.getAccountId());
            }).collect(toList());
        }

        if(statues != null) {
            appointmentCache = appointmentCache.stream().filter(appointmentResponse -> {
                return statues.stream().anyMatch(s -> s.equals(appointmentResponse.getStatus().name()));
            }).collect(toList());
        }

        Page<AppointmentResponse> appointmentResponses = convertListToPage(appointmentCache, pageable);

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

        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());

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

        Page<AppointmentResponse> appointmentResponses;

        List<AppointmentResponse> cacheAppointmentResponses = redisNativeService.getRedisList("appointment-response-list", AppointmentResponse.class);

        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());

        if(!cacheAppointmentResponses.isEmpty()) {
            appointmentResponses = convertListToPage(cacheAppointmentResponses.stream().filter( appointmentResponse -> {
                if(statues != null) {
                    return statues.stream().anyMatch(s -> s.equals(appointmentResponse.getStatus().name()));
                } else {
                    return true;
                }
            }).collect(toList()), pageable);
        } else {
            cacheAppointment();

            CompletableFuture<Page<Appointment>> appointmentsBetweenDateFuture = CompletableFuture.supplyAsync(() -> appointmentRepository.findByAccountIdAndStatusIn(accountId, Objects.requireNonNullElse(statues, new HashSet<>()),pageable));

            appointmentResponses = appointmentsBetweenDateFuture.thenApply(appointments -> appointments.map(this::toAppointmentResponse)).join();
        }


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

        Appointment createSuccess = appointmentRepository.save(appointment);

        Set<Pet> pets = appointmentCreateRequest.getPets().stream()
                .map(petMapper::toEntity)
                .peek(pet -> pet.setAppointment(createSuccess))
                .collect(toSet());

        petRepository.saveAll(pets);

        String createAppointmentStatus = createSuccess.getStatus().name();
        AppointmentResponse appointmentResponse = toAppointmentResponse(createSuccess);

        if (createAppointmentStatus.equals("CHECKED_IN")) {
            messageBrokerService.sendEvent("doctor-appointment-queue", objectMapper.writeValueAsString(appointmentResponse));
        } else {
            if(getDateOnly(appointmentResponse.getAppointmentDate()).equals(getDateOnly(new Date()))) {
                try {
                    queue.add(objectMapper.writeValueAsString(appointmentResponse));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (notification) {
            messageBrokerService.sendEvent("appointment-success-notification-queue", objectMapper.writeValueAsString(appointmentResponse));
        }

        appointmentResponse.setPets(pets.stream().map(petMapper::toDto).collect(toSet()));

        if (appointmentCreateRequest.getAccountId() == null) {
            appointmentResponse.setAccount("GUEST");
        }

        cacheAppointment();

        return appointmentResponse;
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

        appointmentResponse
                .setPets(new HashSet<>(petRepository
                        .findByAppointment_Id(appointment.getId()))
                        .parallelStream().map(petMapper::toDto)
                        .collect(toSet()));
        return appointmentResponse;
    }

    private void cacheAppointment() {
        redisNativeService.deleteRedisList("appointment-response-list");
        CompletableFuture.runAsync(() -> redisNativeService.saveToRedisList("appointment-response-list", appointmentRepository.findAll().stream()
                .map(this::toAppointmentResponse)
                .collect(Collectors.toList()),3600)
        );
    }

    private Page<AppointmentResponse> convertListToPage(List<AppointmentResponse> prescriptionResponseList, Pageable pageable) {

        Sort sort = pageable.getSort();

        // Nếu có yêu cầu sắp xếp, áp dụng Comparator
        if (sort.isSorted()) {
            prescriptionResponseList.sort((a, b) -> {
                for (Sort.Order order : sort) {
                    String property = order.getProperty();
                    int comparison = compareByProperty(a, b, property);

                    // Nếu có sự khác biệt, trả về kết quả sắp xếp
                    if (comparison != 0) {
                        return order.isAscending() ? comparison : -comparison;
                    }
                }
                return 0;
            });
        }

        // Tính toán index của phần tử bắt đầu và kết thúc trong trang
        int start = Math.min((int) pageable.getOffset(), prescriptionResponseList.size());
        int end = Math.min((start + pageable.getPageSize()), prescriptionResponseList.size());

        // Cắt danh sách để chỉ lấy các phần tử trong trang hiện tại
        List<AppointmentResponse> pageContent = prescriptionResponseList.subList(start, end);

        // Trả về PageImpl (Page<PrescriptionResponse>)
        return new PageImpl<>(pageContent, pageable, prescriptionResponseList.size());
    }

    private int compareByProperty(AppointmentResponse a, AppointmentResponse b, String property) {
        // So sánh các thuộc tính theo yêu cầu
        if (property.equals("appointmentDate")) {
            return a.getAppointmentDate().compareTo(b.getAppointmentDate());
        }
        throw new IllegalArgumentException("Unknown property: " + property);
    }
}

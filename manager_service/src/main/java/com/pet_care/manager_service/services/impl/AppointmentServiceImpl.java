package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.*;
import com.pet_care.manager_service.enums.AppointmentStatus;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.repositories.*;
import com.pet_care.manager_service.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    PrescriptionDetailRepository predRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;

    @Override
    public Set<AppointmentHomeDashboardTableResponse> searchAppointmentYesterday(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Appointment> appointments = appointmentRepository.findByAppointmentYesterday(yesterday);
        Set<AppointmentHomeDashboardTableResponse> responses = new HashSet<>();
        for (Appointment appointment: appointments) {
            AppointmentHomeDashboardTableResponse response = appointmentHomeDashboardTableResponse(appointment);
            responses.add(response);
        }
        return responses;
    }
    /*
    * Set<AppointmentHomeDashboardTableResponse>
    * */
//    public Set<AppointmentHomeDashboardTableResponse> searchAppointment(LocalDate create_date, AppointmentStatus status_accept, LocalDate from_date, LocalDate to_date, String search_query ){
//        List<Appointment> appointments = appointmentRepository.searchAppointmentDashboard(create_date,status_accept, from_date, to_date, search_query);
//        Set<AppointmentHomeDashboardTableResponse> responses = new HashSet<>();
//        for (Appointment appointment: appointments) {
//            AppointmentHomeDashboardTableResponse response = appointmentHomeDashboardTableResponse(appointment);
//            responses.add(response);
//        }
//
//        Set<AppointmentHomeDashboardTableResponse> sortedAppointment= responses.stream()
//                .sorted(Comparator.comparing(AppointmentHomeDashboardTableResponse::getAppointmentId).reversed())
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//
//        return sortedAppointment;
//    }

    /*
    * LocalDate create_date : ngay` tao.
    * AppointmentStatus status_accept : trang. thai' chap nhap cua lichj hen
    * LocalDate from_date : tu ngay`
    * LocalDate to_date : den ngay
    * String search_query : search theo ten
    *
    * */
    public PageableResponse<AppointmentHomeDashboardTableResponse> pageSearchAppointment(
            LocalDate create_date, AppointmentStatus status_accept,
            LocalDate from_date, LocalDate to_date, String search_query,
            int page_number, int page_size ){
        Pageable pageable = PageRequest.of(page_number,page_size);
        Page<Appointment> appointments = appointmentRepository.pageSearchAppointmentDashboard(create_date,status_accept, from_date, to_date, search_query, pageable);

        List<AppointmentHomeDashboardTableResponse> listAppointment = appointments.getContent()
                .stream()
                .map(this::appointmentHomeDashboardTableResponse)
                .toList()
                ;
        PageableResponse<AppointmentHomeDashboardTableResponse> pageableResponse = PageableResponse.<AppointmentHomeDashboardTableResponse>builder()
                .content(listAppointment)
                .pageNumber(appointments.getNumber())
                .pageSize(appointments.getSize())
                .totalPages(appointments.getTotalPages())
                .build();
        return pageableResponse;
    }
    @Override
    public AppointmentHomeDashboardTableResponse getAppointmentById(Long id){
        Appointment appointment = appointmentRepository.findById(id).orElse(null);

        return AppointmentHomeDashboardTableResponse.builder().appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointment_date())
                .appointmentTime(appointment.getAppointment_hour())
                .petResponses(petResponses(appointment))
                .customerPrescriptionResponse(customerPrescriptionResponse(appointment))
                .profilesDoctorResponse(profilesDoctorResponse(appointment))
                .appointmentStatus(appointment.getStatus_accept())
                .build();
    }
    @Override
    public AppointmentHomeDashboardTableResponse deleteAppointment(Long id){
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        appointment.setStatus(false);
        appointmentRepository.save(appointment);
        return AppointmentHomeDashboardTableResponse.builder().appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointment_date())
                .appointmentTime(appointment.getAppointment_hour())
                .petResponses(petResponses(appointment))
                .customerPrescriptionResponse(customerPrescriptionResponse(appointment))
                .profilesDoctorResponse(profilesDoctorResponse(appointment))
                .appointmentStatus(appointment.getStatus_accept())
                .build();
    }

    public AppointmentHomeDashboardTableResponse appointmentHomeDashboardTableResponse(Appointment appointment){

        return AppointmentHomeDashboardTableResponse.builder().appointmentId(appointment.getId())
                .appointmentDate(appointment.getAppointment_date())
                .appointmentTime(appointment.getAppointment_hour())
                .petResponses(petResponses(appointment))
                .customerPrescriptionResponse(customerPrescriptionResponse(appointment))
                .profilesDoctorResponse(profilesDoctorResponse(appointment))
                .appointmentStatus(appointment.getStatus_accept())
                .build();
    }
    public Set<PetResponse> petResponses(Appointment appointment){
        Customer customer = appointment.getCustomer();
        Set<Pet> pets = customer.getPets();
        Set<PetResponse> petResponses = new HashSet<>();
        for(Pet pet : pets){
//            Pet p = petRepository.findById(pet.getId()).get();
            Species species = pet.getSpecies();
            SpeciesResponse speciesResponse =  SpeciesResponse.builder()
                    .id(pet.getId())
                    .name(species.getName())
                    .build();
            petResponses.add(
                    PetResponse.builder()
                            .id(pet.getId())
                            .name(pet.getName())
                            .weight(pet.getWeight())
                            .age(pet.getAge())
                            .speciesResponse(speciesResponse)
                            .prescriptionResponses(prescriptionByPetId(pet.getId()))
                            .build()
            );
        }
        return petResponses;
    }
    public CustomerPrescriptionResponse customerPrescriptionResponse(Appointment appointment){
        return CustomerPrescriptionResponse.builder()
                .id(appointment.getCustomer().getId())
                .first_name(appointment.getCustomer().getFirst_name())
                .last_name(appointment.getCustomer().getLast_name())
                .phone_number(appointment.getCustomer().getPhone_number())
                .build();
    }

    public ProfilesDoctorResponse profilesDoctorResponse(Appointment appointment){
        Customer customer = appointment.getCustomer();
        Set<Pet> pets = customer.getPets();
        long profileId = 0;
        for(Pet pet : pets){
            Prescription prescription = pet.getPrescriptions();
            if(prescription == null){
                Profile profile = new Profile();
                System.out.println("Check profiles 1: " + profile);
                continue;
            }
            Profile profile = prescription.getProfile();
            profileId = profile.getId();
            System.out.println("Check profiles 2: " + profile);
        }
        Profile prf = new Profile();
        Optional<Profile> profile = profileRepository.findById(profileId);
        if (profile.isPresent())
        {
            prf = profile.get();
        }
        System.out.println("Check profiles 3: " + profile);
        return ProfilesDoctorResponse.builder()
                .id(prf.getId())
                .first_name(prf.getFirst_name())
                .last_name(prf.getLast_name())
                .build();
    }
    public Set<PrescriptionResponse> prescriptionByPetId(Long id) {

        List<Object[]> pres = prescriptionRepository.getPrescriptionByPetId(id);
        if (pres == null) {
            throw new AppException(ErrorCode.PRESCRIPTION_NOTFOUND);
        }
        Set<PrescriptionDetailResponse> presriptionDetailResponses;
        Set<PrescriptionResponse> prescriptionResponses = new HashSet<>();
        for (Object[] obj : pres) {
            Prescription prescription = prescriptionRepository.findById((Long) obj[0]).get();
            List<Prescription_Details> prescription_details = prescriptionDetailRepository.findByPrescription(prescription);
            System.out.println("Check prescription_details : " + prescription_details.size());
//
            presriptionDetailResponses = prescription_details.stream()
                    .map(this::convertToPrescriptionDetailResponse)
                    .collect(Collectors.toSet());

            prescriptionResponses.add(PrescriptionResponse.builder()
                    .id((Long) obj[0])
                    .create_date((LocalDate) obj[1])
                    .note((String) obj[2])
                    .disease_name((String) obj[3])
                    .prescriptionDetailResponse(presriptionDetailResponses)
                    .build()
            );
        }
        return prescriptionResponses;
    }
    public PrescriptionDetailResponse convertToPrescriptionDetailResponse(Prescription_Details prescription_details){
        Medicine medicine = medicineRepository.findById(prescription_details.getMedicine().getId()).get();
        MedicineResponse medicineResponse = convertToMedicineResponse(medicine);
        return PrescriptionDetailResponse.builder()
                .id(prescription_details.getId())
                .quantity(prescription_details.getQuantity())
                .medicineResponse(medicineResponse)
                .build();
    }
    public MedicineResponse convertToMedicineResponse(Medicine medicine){
        return MedicineResponse.builder()
                .id( medicine.getId())
                .name(medicine.getName())
                .build();
    }

    public Appointment findByAppointmentId(Long id){
        Appointment appointment = appointmentRepository.findById(id).get();
        return appointment;
    }
}

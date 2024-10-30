package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.*;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.repositories.*;
import com.pet_care.manager_service.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    InvoiceRepository invoiceRepository;

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
    AppointmentRepository appointmentRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;

    public CustomerHomeDashboardResponse getCountCustomer(){
        LocalDate now = LocalDate.now();
        List<Object[]> listCustomer = invoiceRepository.getAllCustomerToday(now);
        Long count = (long) listCustomer.size();
        System.out.println("Check list customer: " + count);
        return CustomerHomeDashboardResponse.builder()
                .countCustomer(count)
                .build();
    }

    public AppointmentHomeDashboardResponse getCountAppointment(){
        LocalDate now = LocalDate.now();
//      LocalDate now = LocalDate.parse("2024-10-01");
//        List<Object[]> listAppointment = invoiceRepository.getAllAppointmentToday(now);
        List<Appointment> listAppointment = invoiceRepository.findByAppointmentDate(now);
        Long count = (long) listAppointment.size();
        System.out.println("Check list appointment: " + count);
        return AppointmentHomeDashboardResponse.builder()
                .countAppointment(count)
                .build();
    }

    public InvoiceHomeDashboardResponse getCountInvoice(){
        LocalDate now = LocalDate.now();
        List<Object[]> listInvoice = invoiceRepository.getAllInvoiceToday(now);
        Long count = (long) listInvoice.size();
        System.out.println("Check list invoice: " + count);
        return InvoiceHomeDashboardResponse.builder()
                .countInvoice(count)
                .build();
    }


    public CustomerPrescriptionResponse getCustomerPrescription(Long prescriptionId){
        Prescription presc = prescriptionRepository.findById(prescriptionId).orElse(null);
        Pet pet = petRepository.findById(presc.getPet().getId()).get();
        Customer customer = customerRepository.findById(pet.getId()).get();
        if(customer == null){
            throw new AppException(ErrorCode.CUSTOMER_NOTFOUND);
        }
        return CustomerPrescriptionResponse.builder()
                .id(customer.getId())
                .first_name(customer.getFirst_name())
                .last_name(customer.getLast_name())
                .phone_number(customer.getPhone_number())
                .build();
    }
    public PrescriptionHomeDashboardResponse prescriptionHomeDashboardResponse(Long id){
        Set<PrescriptionDetailResponse> prescriptionDetailResponseSet = new HashSet<>();
        Prescription prescription = prescriptionRepository.findById(id).get();
        List<Prescription_Details> prescription_details = predRepository.findByPrescription(prescription);
        prescriptionDetailResponseSet = prescription_details.stream()
                    .map(this::convertPrescriptionDetailResponse)
                    .collect(Collectors.toSet());

        Profile profile = prescription.getProfile();
        ProfilesDoctorResponse profilesResponse = ProfilesDoctorResponse.builder()
                .id(profile.getId())
                .first_name(profile.getFirst_name())
                .last_name(profile.getLast_name())
                .build();
        return PrescriptionHomeDashboardResponse.builder()
                .prescriptionId(id)
                .customerPrescription(getCustomerPrescription(id))
                .profilesDoctor(profilesResponse)
                .petPrescription(convertPetPrescriptionResponse(prescription))
                .prescriptionDetails(prescriptionDetailResponseSet)
                .build();
    }
    public PrescriptionDetailResponse convertPrescriptionDetailResponse(Prescription_Details prescription_details){
        Medicine medicine = medicineRepository.findById(prescription_details.getMedicine().getId()).get();
        MedicineResponse medicineResponse =  MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .build();
        return PrescriptionDetailResponse.builder()
                .id(prescription_details.getId())
                .quantity(prescription_details.getQuantity())
                .medicineResponse(medicineResponse)
                .description(prescription_details.getNote())
                .build();
    }

    public PetPrescriptionResponse convertPetPrescriptionResponse(Prescription prescription){
        Pet pet = petRepository.findById(prescription.getPet().getId()).get();
        Species species = pet.getSpecies();
        SpeciesResponse speciesResponse = SpeciesResponse.builder()
                .id(species.getId())
                .name(species.getName())
                .build();
        return PetPrescriptionResponse.builder()
                .id(prescription.getId())
                .name(pet.getName())
                .weight(pet.getWeight())
                .age(pet.getAge())
                .speciesResponse(speciesResponse)
                .build();
    }
    public Set<PrescriptionHomeDashboardResponse> getPrescriptionHomeDashboard(){
        LocalDate now = LocalDate.now();
        List<Object[]> listPres = prescriptionRepository.getPrescriptionToday(now);
        Set<PrescriptionHomeDashboardResponse> prescriptionHomeDashboardResponseSet = new HashSet<>();
        for(Object[] objects : listPres){
            Long predId = (Long) objects[0];
            prescriptionHomeDashboardResponseSet.add(prescriptionHomeDashboardResponse(predId));
        }
        Set<PrescriptionHomeDashboardResponse> sortPrescriptionHomeDashboardResponseSet = prescriptionHomeDashboardResponseSet
                .stream().sorted(Comparator.comparing(PrescriptionHomeDashboardResponse::getPrescriptionId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortPrescriptionHomeDashboardResponseSet;
    }

    @Override
    public DashboardResponse getDashboardHome() {
        return DashboardResponse.builder()
                .customers(getCountCustomer())
                .appointments(getCountAppointment())
                .invoices(getCountInvoice())
                .prescriptions(getPrescriptionHomeDashboard())
                .build();
    }

    @Override
    public Set<AppointmentHomeDashboardTableResponse> listAppointmentHomeDashboard(){
        LocalDate now = LocalDate.now();
        List<Appointment> listApp = appointmentRepository.getAllAppointmentToDay(now);
        Set<AppointmentHomeDashboardTableResponse> listAppointment = new HashSet<>();
        for(Appointment appointment : listApp){
            listAppointment.add(appointmentHomeDashboardTable(appointment));
        }
        Set<AppointmentHomeDashboardTableResponse> sordAppointment = listAppointment.stream()
                .sorted(Comparator.comparing(AppointmentHomeDashboardTableResponse::getAppointmentId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sordAppointment;
    }

    public AppointmentHomeDashboardTableResponse appointmentHomeDashboardTable(Appointment appointment) {

        return AppointmentHomeDashboardTableResponse.builder()
                .appointmentId(appointment.getId())
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
        Set<PetResponse> sortPet = petResponses.stream()
                .sorted(Comparator.comparing(PetResponse::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortPet;
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

    public Set<PrescriptionResponse> prescriptionResponse(Appointment appointment){
        Customer customer = appointment.getCustomer();
        Set<Pet> pets = customer.getPets();
        Set<PrescriptionDetailResponse> presriptionDetailResponses ;
        Set<PrescriptionResponse> prescriptionResponses = new HashSet<>();
        for (Pet pet : pets){
            List<Object[]> prescriptions = prescriptionRepository.getPrescriptionByPetId(pet.getId());
            for(Object[] obj : prescriptions){
                Prescription prescription = prescriptionRepository.findById((Long) obj[0]).get();
                List<Prescription_Details> prescription_details = prescriptionDetailRepository.findByPrescription(prescription);

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
        }
        return prescriptionResponses;
    }

    public MedicineResponse convertToMedicineResponse(Medicine medicine){
        return MedicineResponse.builder()
                .id( medicine.getId())
                .name(medicine.getName())
                .build();
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
}

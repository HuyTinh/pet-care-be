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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        List<Object[]> listAppointment = invoiceRepository.getAllAppointmentToday(now);
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
    public DashboardResponse getHomeDashboard(){
        return DashboardResponse.builder()
                .customers(getCountCustomer())
                .appointments(getCountAppointment())
                .invoices(getCountInvoice())
                .prescriptions(getPrescriptionHomeDashboard())
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
        return prescriptionHomeDashboardResponseSet;
    }
}

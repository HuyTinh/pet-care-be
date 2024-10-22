package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.*;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.mapper.CustomerMapper;
import com.pet_care.manager_service.mapper.PetMapper;
import com.pet_care.manager_service.mapper.PrescriptionMapper;
import com.pet_care.manager_service.mapper.ServiceMapper;
import com.pet_care.manager_service.repositories.*;
import com.pet_care.manager_service.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SpeciesRepository speciesRepository;

    @Autowired
    ServicesRepository servicesRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    PetMapper petMapper;

    @Autowired
    PrescriptionMapper prescriptionMapper;

    @Autowired
    CustomerMapper customerMapper;


    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    PrescriptionDetailRepository prescriptionDetailRepository;

    @Autowired
    private MedicineRepository medicineRepository;


    public List<Customer> getAllCustomer(){
        return customerRepository.findAll();
    }

    public <S extends Customer> S save(S entity) {
        return customerRepository.save(entity);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public List<CustomerPetAndServiceResponse> getAllCustomers() {

        List<Object[]> listCustomers = customerRepository.getAllCustomer();
        System.out.println("Check list Customer : " + listCustomers);
        if (listCustomers.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
        }
        return listCustomers.stream()
                .map(this::convertCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerPetAndServiceResponse> getAllCustomersTrue() {
        List<Object[]> listCustomers = customerRepository.getAllCustomerByStatusTrue();
        System.out.println("Check list Customer : " + listCustomers);

        if (listCustomers.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
        }
        return listCustomers.stream()
                .map(this::convertCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerPetAndServiceResponse deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).get();
        System.out.println("Check customer : " + customer.getId());
        customer.setStatus(false);
        customerRepository.save(customer);
        return customerMapper.toCustomerResponse(customer);
    }

    public ServiceResponse findServiceById(Long id) {
        Services service = servicesRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.SERVICE_NOTFOUND));
        return serviceMapper.toServiceResponse(service);
    }

    public PetResponse toPetResponseById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.PET_NOTFOUND));
        return PetResponse.builder()
                .id(pet.getId())
                .weight(pet.getWeight())
                .age(pet.getAge())
                .speciesResponse(convertToSpeciesResponse(pet))
                .prescriptionResponses(convertPrescriptionByPetId(pet.getId()))
                .build();
    }

    public Set<PrescriptionResponse> convertPrescriptionByPetId(Long id){

        List<Object[]> pres = prescriptionRepository.getPrescriptionByPetId(id);
        if(pres == null){
            throw new AppException(ErrorCode.PRESCRIPTION_NOTFOUND);
        }
        Set<PrescriptionDetailResponse> presriptionDetailResponses;
        Set<PrescriptionResponse> prescriptionResponses = new HashSet<>();
        for(Object[] obj : pres){
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
                    .prescriptionDetailResponse(presriptionDetailResponses)
                    .build()
            );
        }

        return prescriptionResponses;
    }

    public CustomerPetAndServiceResponse convertCustomerResponse(Object[] row){
        List<Object[]> listPet = petRepository.getPetByCustomerId((Long) row[0]);
        System.out.println("Check listPet : " + listPet.size());
        if(listPet.isEmpty()){
            throw new AppException(ErrorCode.PET_NOTFOUND);
        }
//      Lấy pet
        Set<PetResponse> petResponses = new HashSet<>();

        for(Object[] obj : listPet) {
            System.out.println("Check object : " + obj[0]);
            Long petId = ((Number) obj[0]).longValue();
            Pet pet = petRepository.findById(petId).get();
            System.out.println("Check pets : " + pet);
            PetResponse petResponse = PetResponse.builder()
                    .id(pet.getId())
                    .name(pet.getName())
                    .weight(pet.getWeight())
                    .age(pet.getAge())
                    .prescriptionResponses(convertPrescriptionByPetId(pet.getId())) // get Prescription
                    .speciesResponse(convertToSpeciesResponse(pet)) // get Species
                    .build();
            System.out.println("Check pet Detail : " + petResponse);
            petResponses.add(petResponse);
        }
        System.out.println("Check list Pet Response: " + petResponses);

        Set<ServiceResponse> service_set = new HashSet<>();
        List<Object[]> listServices = servicesRepository.findServicesByCustomerId( (Long) row[0]);
        if(listServices.isEmpty()){
            throw new AppException(ErrorCode.SERVICE_NOTFOUND);
        }
//        Lấy service
        for(Object[] obj : listServices){
            Services services = servicesRepository.findServicesByName((String) obj[0]).get();
            System.out.println("Check services : " + services);
            service_set.add(ServiceResponse.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .price(services.getPrice())
                        .build());
        }
        System.out.println("Check customer : " + row[0]);

        return CustomerPetAndServiceResponse.builder()
                .id( (Long) row[0])
                .last_name((String) row[1])
                .first_name((String) row[2])
                .phone_number((String) row[3])
                .petResponses(petResponses)
                .serviceResponses(service_set)
                .build();
    }

    public SpeciesResponse convertToSpeciesResponse(Pet pet){
        Optional<Object[]> species = speciesRepository.findSpeciesByPetId(pet.getId());
        System.out.println("Check Species : " + species.get()[0]);
        System.out.println("Check Species : " + species.get());
        if (species.isPresent()) {
            Object[] speciesArray = (Object[]) species.get()[0];

            Long speciesId = (Long) speciesArray[0]; // Chuyển đổi ID
            String speciesName = (String) speciesArray[1]; // Chuyển đổi tên

            System.out.println("Species ID: " + speciesId);
            System.out.println("Species Name: " + speciesName);
        } else {
            System.out.println("No species found for pet ID: " + pet.getId());
        }

        SpeciesResponse build = SpeciesResponse.builder()
                .id((Long) ((Object[]) species.get()[0])[0])
                .name((String) ((Object[]) species.get()[0])[1])
                .build();
        return build;
    }

    public MedicineResponse convertToMedicineResponse(Medicine medicine){
        System.out.println("Check Medicine convert : " + medicine);
        return MedicineResponse.builder()
                .id( medicine.getId())
                .name(medicine.getName())
                .build();
    }
    public PrescriptionDetailResponse convertToPrescriptionDetailResponse(Prescription_Details prescription_details){
        Medicine medicine = medicineRepository.findById(prescription_details.getMedicine().getId()).get();
        MedicineResponse medicineResponse = convertToMedicineResponse(medicine);
        System.out.println("Check Medicine Pres : " + medicineResponse);
        return PrescriptionDetailResponse.builder()
                .id(prescription_details.getId())
                .quantity(prescription_details.getQuantity())
                .medicineResponse(medicineResponse)
                .build();
    }

}

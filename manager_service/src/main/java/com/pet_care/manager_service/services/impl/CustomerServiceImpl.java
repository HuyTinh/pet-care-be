package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.entity.Customer;
import com.pet_care.manager_service.entity.Pet;
import com.pet_care.manager_service.entity.Services;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.mapper.PetMapper;
import com.pet_care.manager_service.mapper.PrescriptionMapper;
import com.pet_care.manager_service.mapper.ServiceMapper;
import com.pet_care.manager_service.repositories.*;
import com.pet_care.manager_service.services.AppointmentService;
import com.pet_care.manager_service.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    PetMapper petMapper;

    @Autowired
    PrescriptionMapper prescriptionMapper;

    @Autowired
    PrescriptionRepository prescriptionRepository;


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
        if (listCustomers.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOTFOUND);
        }
        return listCustomers.stream()
                .map(this::convertCustomerResponse)
                .collect(Collectors.toList());
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
                .species(convertToSpeciesResponse(pet))
                .prescription(convertPrescriptionByPetId(pet.getId()))
                .build();
    }

    public PrescriptionResponse convertPrescriptionByPetId(Long id){
        Object[] pres = prescriptionRepository.getPrescriptionByPetId(id);
        if(pres == null){
            throw new AppException(ErrorCode.PRESCRIPTION_NOTFOUND);
        }
        return PrescriptionResponse.builder()
                .id( (Long) pres[0])
                .create_date( (Date) pres[1])
                .note((String) pres[2])
                .build();
    }

    public CustomerPetAndServiceResponse convertCustomerResponse(Object[] row){
        List<Object[]> listPet = petRepository.getAllPetByCustomerId((Long) row[0]);
        if(listPet.isEmpty()){
            throw new AppException(ErrorCode.PET_NOTFOUND);
        }
//      Lấy pet
        Set<PetResponse> pets_set = new HashSet<>();
        for(Object[] obj : listPet){
            Pet pet = petRepository.findById((Long) obj[0]).get();
            PetResponse petResponse = PetResponse.builder()
                    .id(pet.getId())
                    .name(pet.getName())
                    .weight(pet.getWeight())
                    .age(pet.getAge())
                    .prescription(convertPrescriptionByPetId(pet.getId())) // get Prescription
                    .species(convertToSpeciesResponse(pet)) // get Species
                    .build();
            pets_set.add(petResponse);
        }

        Set<ServiceResponse> service_set = new HashSet<>();
        List<Object[]> listServices = servicesRepository.findServicesByCustomerId( (Long) row[0]);
        if(listServices.isEmpty()){
            throw new AppException(ErrorCode.SERVICE_NOTFOUND);
        }
//        Lấy service
        for(Object[] obj : listServices){
            Services services = servicesRepository.findServicesByName((String) obj[0]).get();
            service_set.add(ServiceResponse.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .price(services.getPrice())
                        .build());
        }
        return CustomerPetAndServiceResponse.builder()
                .id( (Long) row[0])
                .last_name((String) row[1])
                .first_name((String) row[2])
                .phone_number((String) row[3])
                .petResponses(pets_set)
                .serviceResponses(service_set)
                .build();
    }

    public SpeciesResponse convertToSpeciesResponse(Pet pet){
        Object[] species = speciesRepository.findByPetId(pet.getId());
        return SpeciesResponse.builder()
                .id( (Long) species[0])
                .name((String) species[1])
                .build();
    }

}

package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.request.ServicesRequest;
import com.pet_care.manager_service.dto.response.ServiceCRUDResponse;
import com.pet_care.manager_service.dto.response.ServiceResponse;
import com.pet_care.manager_service.dto.response.ServiceTypeResponse;
import com.pet_care.manager_service.entity.Service_Type;
import com.pet_care.manager_service.entity.Services;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.mapper.ServiceMapper;
import com.pet_care.manager_service.repositories.ServiceTypeRepository;
import com.pet_care.manager_service.repositories.ServicesRepository;
import com.pet_care.manager_service.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    ServicesRepository servicesRepository;

    @Autowired
    ServiceTypeRepository serviceTypeRepository;

    @Autowired
    ServiceMapper serviceMapper;

    public ServiceCRUDResponse createService(
           ServicesRequest servicesRequest
    ){
        Service_Type serviceType = serviceTypeRepository.findById(servicesRequest.getService_type_id()).get();
        Services serviceBuilder = Services.builder()
                .name(servicesRequest.getName())
                .price(servicesRequest.getPrice())
                .status(servicesRequest.getStatus())
                .service_type(serviceType)
                .build();
        ServiceTypeResponse serviceTypeResponse = ServiceTypeResponse.builder()
                .id(serviceType.getId())
                .name(serviceType.getName())
                .build();
        Services service =  servicesRepository.save(serviceBuilder);
        ServiceCRUDResponse response = serviceMapper.toServiceCRUDResponse(service);
        response.setServiceTypeResponse(serviceTypeResponse);
        return response;
    }

    public Set<ServiceCRUDResponse> getAllServices(){
        List<Services> services = servicesRepository.getAllServices();
        Set<ServiceCRUDResponse> servicesResponses = new HashSet<>();
        for (Services service : services){
            ServiceCRUDResponse response = getServiceById(service.getId());
            servicesResponses.add(response);
        }
        Set<ServiceCRUDResponse> sortService = servicesResponses.stream()
                .sorted(Comparator.comparing(ServiceCRUDResponse::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return sortService;
    }

    public ServiceCRUDResponse getServiceById(Long id) {
        Optional<Services> servicesOptional = servicesRepository.findById(id);
        if (servicesOptional.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOTFOUND);
        }
        Services services = servicesOptional.get();
        return ServiceCRUDResponse.builder()
                .id(services.getId())
                .name(services.getName())
                .price(services.getPrice())
                .serviceTypeResponse(getServiceTypeByServiceId(services.getId()))
                .build();
    }
    public ServiceTypeResponse getServiceTypeByServiceId(Long service_id) {
        Optional<Services> servicesOptional = servicesRepository.findById(service_id);
        if (servicesOptional.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOTFOUND);
        }
        Services services = servicesOptional.get();
        Service_Type service_type = services.getService_type();
        return ServiceTypeResponse.builder()
                .id(service_type.getId())
                .name(service_type.getName())
                .build();
    }

    public ServiceResponse deleteServiceById(Long id) {
        Optional<Services> servicesOptional = servicesRepository.findById(id);
        Services services = servicesOptional.get();
        services.setStatus(false);
        servicesRepository.save(services);

        return ServiceResponse.builder()
                .id(services.getId())
                .name(services.getName())
                .price(services.getPrice())
                .build();
    }

}

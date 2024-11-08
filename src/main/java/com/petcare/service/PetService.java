package com.petcare.service;

import com.petcare.dto.request.SearchRequest;
import com.petcare.dto.response.PetDetailResponse;
import com.petcare.dto.response.PetResponse;
import com.petcare.entity.Pet;
import com.petcare.exception.APIException;
import com.petcare.exception.APIExceptionHandler;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.PetDetailMapper;
import com.petcare.mapper.PetMapper;
import com.petcare.repository.PetRepository;
import com.petcare.utils.ExcelExport;
import com.petcare.utils.PetSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    private static final int MAX_SIZE = 50;

    public Page<Pet> getAllPets(int getPage) {

        getPage = getPage <= 1 ? 0 : getPage - 1;

        Pageable pageable = PageRequest.of(getPage, MAX_SIZE, Sort.by("id").descending());
        Page<Pet> pets = petRepository.findAll(pageable);
        return pets;
    }

    public List<Pet> getAllPetsNoPage() {

        List<Pet> pets = petRepository.findAll();
        return pets;
    }

    public Pet getPetById(long petId) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new APIException(ErrorCode.PET_ID_NOT_FOUND));

        return pet;
    }

    public ByteArrayInputStream getActualData() throws IOException {

        List<Pet> pets = getAllPetsNoPage();
        List<PetResponse> petResponses = PetMapper.INSTANCE.mapperPetsToPetsResponse(pets);
        ByteArrayInputStream inputStream = ExcelExport.exportExcel(petResponses);

        return inputStream;
    }

    public List<Pet> searchPets(SearchRequest searchRequest) {

        Specification<Pet> searchSpec = PetSpecification
                                        .findPetByAny(searchRequest.getFieldSearch(), searchRequest.getValueSearch());
        List<Pet> pets = petRepository.findAll(searchSpec);

        return pets;
    }

}

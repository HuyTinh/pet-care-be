package com.pet_care.search_service.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.pet_care.search_service.model.Pet;
import com.pet_care.search_service.service.ESService;
import com.pet_care.search_service.service.PetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetController {

     PetService petService;

     ESService esService;

    @GetMapping()
    
    Iterable<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @PostMapping()
    
    Pet insertPet( @RequestBody Pet pet) {
        return petService.insertPet(pet);
    }

    
    @GetMapping("/autoSuggestion/{partialPetName}")
    List<String> autoSuggestion(@PathVariable("partialPetName") String partialPetName) throws IOException {
        SearchResponse<Pet> searchResponse = esService.autoSuggestPet(partialPetName);
        List<Hit<Pet>> hitList = searchResponse.hits().hits();
        return hitList.stream().map(Hit::source).filter(Objects::nonNull).map(Pet::getName).toList();
    }
}

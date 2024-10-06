package com.pet_care.search_service.repository;

import com.pet_care.search_service.model.Pet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PetRepository extends ElasticsearchRepository<Pet, Long> {
}

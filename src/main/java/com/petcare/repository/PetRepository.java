package com.petcare.repository;

import com.petcare.entity.Pet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends ElasticsearchRepository<Pet, Long> {
}

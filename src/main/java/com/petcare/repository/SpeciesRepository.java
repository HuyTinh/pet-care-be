package com.petcare.repository;

import com.petcare.entity.Species;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends ElasticsearchRepository<Species, Long> {
}

package com.petcare.repository;

import com.petcare.entity.Owner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends ElasticsearchRepository<Owner, Long> {
}

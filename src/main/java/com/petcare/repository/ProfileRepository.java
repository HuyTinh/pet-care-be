package com.petcare.repository;

import com.petcare.entity.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends ElasticsearchRepository<Profile, Long> {
}

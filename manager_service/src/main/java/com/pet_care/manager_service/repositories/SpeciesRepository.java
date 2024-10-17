package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpeciesRepository extends JpaRepository<Species, Long> {

    Optional<Species> findByName(String name);

    @Query(value = " SELECT sp.id, sp.name " +
            " FROM Species sp " +
            " JOIN sp.pet p " +
            " WHERE p.id = :id ")
    Object[] findByPetId(@Param("id") Long id);
}

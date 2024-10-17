package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query
            (value = " SELECT p.id, p.name, p.weight, p.age, sp.name as species_name " +
                    " FROM Pet p " +
                    " JOIN p.customer c " +
                    " JOIN p.species sp " +
                    " WHERE c.id = :id ")
    List<Object[]> getPetByCustomerId(@Param("id") Long id);
}

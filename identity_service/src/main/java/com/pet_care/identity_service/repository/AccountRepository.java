package com.pet_care.identity_service.repository;

import com.pet_care.identity_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * @param email
     * @return
     */
    Optional<Account> findByEmail(String email);

    /**
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
}
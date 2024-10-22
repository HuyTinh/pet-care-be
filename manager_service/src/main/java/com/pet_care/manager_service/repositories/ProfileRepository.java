package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Account;
import com.pet_care.manager_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByAccount(Account account);
}

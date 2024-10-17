package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Account;
import com.pet_care.manager_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account, Long> {
//      Get All Employee
    @Query(value = "SELECT a.id, a.email, a.password, a.status" +
            "   FROM Account a " +
            "   JOIN a.profile p " +
            "   JOIN p.role r" +
            " WHERE r.id NOT IN (1, 6) " +
            " ORDER BY a.id ")
    List<Object[]> getAllEmployee();

//    Get All By Role
    @Query(value = "SELECT a.id, a.email, a.password, a.status" +
            "   FROM Account a " +
            "   JOIN a.profile p " +
            "   JOIN p.role r" +
            " WHERE r.id = :id " +
            " ORDER BY a.id ")
    List<Object[]> getAllByRole(@Param("id") Long id);

    //      Get All Employee True
    @Query(value = "SELECT a.id, a.email, a.password, a.status" +
            "   FROM Account a " +
            "   JOIN a.profile p " +
            "   JOIN p.role r" +
            " WHERE r.id NOT IN (1, 6) AND a.status = true " +
            " ORDER BY a.id ")
    List<Object[]> getAllEmployeeTrue();

//    List<Account> getAllAccountByRoleNotInAndStatus(@Param("roles") Set<Role> roles, @Param("status") boolean status);
}

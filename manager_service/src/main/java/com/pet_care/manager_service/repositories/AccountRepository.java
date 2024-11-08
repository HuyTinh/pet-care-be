package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Account;
import com.pet_care.manager_service.entity.Role;
import com.pet_care.manager_service.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            " WHERE  (:role_name IS NULL OR r.name = :role_name) and r.status = true" +
            " ORDER BY a.id ")
    List<Object[]> getAllByRole(@Param("role_name") RoleEnum name);

    //      Get All Employee True
//    @Query(value = "SELECT a.id, a.email, a.password, a.status" +
//            "   FROM Account a " +
//            "   JOIN a.profile p " +
//            "   JOIN p.role r" +
//            " WHERE r.id NOT IN (1, 6) AND a.status = true " +
//            " ORDER BY a.id ")
//    List<Object[]> getAllEmployeeTrue();
    @Query(value = "SELECT a " +
            "   FROM Account a " +
            "   JOIN a.profile p " +
            "   JOIN p.role r" +
            " WHERE r.id NOT IN (1, 6) AND a.status = true AND " +
            "       (:searchQuery IS NULL OR ( " +
            "             p.first_name LIKE %:searchQuery%  " +
            "           OR p.last_name LIKE %:searchQuery% " +
            "           OR p.phone_number LIKE %:searchQuery% " +
            "           OR p.email LIKE %:searchQuery%  " +
            "           )" +
            "       )" +
            " ORDER BY a.id ")
    Page<Account> getAllEmployeeTrue(
            @Param("searchQuery") String search_query,
            Pageable pageable
    );

}

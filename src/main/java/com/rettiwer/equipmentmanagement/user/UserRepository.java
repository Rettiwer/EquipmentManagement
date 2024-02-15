package com.rettiwer.equipmentmanagement.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);


    @Query(value = "SELECT usr.*, sv_emp.supervisor_id FROM users usr LEFT JOIN  supervisor_employee sv_emp on usr.id = sv_emp.employee_id" +
            " WHERE MATCH (usr.firstname, usr.lastname) " +
            "AGAINST (CONCAT(:fullName, '*') IN BOOLEAN MODE)", nativeQuery = true)
    List<User> findFullTextSearchFullName(@Param("fullName") String fullName);
}
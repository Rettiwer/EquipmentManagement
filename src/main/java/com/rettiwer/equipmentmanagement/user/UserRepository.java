package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query( "select distinct u from User u join u.roles r where r.name in :roles" )
    List<User> findByRoles(@Param("roles") List<Role.UserRole> roles);

    @Query(value = "SELECT usr.*, sv_emp.supervisor_id FROM users usr LEFT JOIN supervisor_employee sv_emp on usr.id = sv_emp.employee_id" +
            " WHERE MATCH (usr.firstname, usr.lastname) " +
            "AGAINST (CONCAT(:fullName, '*') IN BOOLEAN MODE)", nativeQuery = true)
    List<User> searchUserByFullName(@Param("fullName") String fullName);

    @Query(value = "SELECT usr.*, sv_emp.supervisor_id FROM users usr " +
            "LEFT JOIN supervisor_employee sv_emp ON usr.id = sv_emp.employee_id " +
            "JOIN user_roles ur ON usr.id = ur.user_id " +
            "JOIN roles r ON ur.role_id = r.id " +
            "WHERE MATCH (usr.firstname, usr.lastname) AGAINST (CONCAT(:fullName, '*') IN BOOLEAN MODE) " +
            "AND r.name = 'ROLE_SUPERVISOR'", nativeQuery = true)
    List<User> searchSupervisorByFullName(@Param("fullName") String fullName);
}
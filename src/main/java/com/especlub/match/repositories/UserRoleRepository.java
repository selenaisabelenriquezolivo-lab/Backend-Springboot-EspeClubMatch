package com.especlub.match.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.especlub.match.models.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
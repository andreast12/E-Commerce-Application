package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(String roleName);
}

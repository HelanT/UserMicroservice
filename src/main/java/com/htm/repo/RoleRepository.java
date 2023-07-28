package com.htm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htm.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByRole(String string);

}

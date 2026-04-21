package edu.miu.cs.cs489.courseproject.dental.repository;

import edu.miu.cs.cs489.courseproject.dental.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);
}

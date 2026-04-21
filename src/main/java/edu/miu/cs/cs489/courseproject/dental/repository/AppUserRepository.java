package edu.miu.cs.cs489.courseproject.dental.repository;

import edu.miu.cs.cs489.courseproject.dental.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
}

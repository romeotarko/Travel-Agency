package com.lufthansa.travelagency.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByUsernameAndEmail(String username, String email);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}

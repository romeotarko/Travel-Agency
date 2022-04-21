package com.lufthansa.travelagency.user;

import com.lufthansa.travelagency.exception.TripAgencyApplicationException;
import com.lufthansa.travelagency.role.ERoleType;
import com.lufthansa.travelagency.role.Role;
import com.lufthansa.travelagency.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;


    public User create(UserDto userDto) {
        checkIfUserExistsByUsernameAndEmail(userDto.getUsername(), userDto.getEmail());
        log.info("Saving new user {} to the database", userDto.getUsername());
        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERoleType.ROLE_USER).get());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRoles(roles);

        return userRepository.saveAndFlush(user);
    }

    public Optional<User> getById(Long id) {
        log.info("Get user {} from database", id);
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        log.info("Getting users from database");
        return userRepository.findAll();
    }

    public void delete(Long id) {
        checkIfUserExists(id);
        userRepository.deleteById(id);
    }

    private void checkIfUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            log.debug("User with id {} does not exist!", id);
            throw new TripAgencyApplicationException("User with id: " + id + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }

    private void checkIfUserExistsByUsernameAndEmail(String username, String email) {
        if (userRepository.existsByUsernameAndEmail(username, email)) {
            throw new TripAgencyApplicationException("User already exists", HttpStatus.BAD_REQUEST);
        }
    }

}

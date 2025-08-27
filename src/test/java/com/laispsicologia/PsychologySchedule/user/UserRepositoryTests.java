package com.laispsicologia.PsychologySchedule.user;

import com.laispsicologia.PsychologySchedule.factory.UserFactory;
import com.laispsicologia.PsychologySchedule.user.entity.Role;
import com.laispsicologia.PsychologySchedule.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository repository;

    private Long validId, invalidId, countTotalProfessionals;

    private String validUsername, invalidUsername;

    Pageable pageable;


    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 2000L;
        countTotalProfessionals = repository.count();

        validUsername = "paulo@gmail.com";
        invalidUsername = "invalido@gmail.com";

        pageable = PageRequest.of(0, 10);
    }


    @Test
    public void findAllShouldReturnPageOfUSer() {
        Page<User> result = repository.findAll(pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProfessionals, result.getTotalElements());
    }

    @Test
    public void findByIdShouldReturnNotEmptyOptionalWhenValidId() {
        Optional<User> result = repository.findById(validId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(validId, result.get().getId());
        Assertions.assertEquals("Paulo", result.get().getName());
        Assertions.assertEquals("paulo@gmail.com", result.get().getEmail());
        Assertions.assertEquals(Role.ADMIN, result.get().getRole());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenInvalidId() {
        Optional<User> result = repository.findById(invalidId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findByUsernameShouldReturnNotEmptyOptionalWhenValidUserName() {
        Optional<User> result = repository.findByUsername(validUsername);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get().getId());
        Assertions.assertEquals("Paulo", result.get().getName());
        Assertions.assertEquals(validUsername, result.get().getEmail());
        Assertions.assertEquals(Role.ADMIN, result.get().getRole());
    }

    @Test
    public void findByUsernameShouldReturnEmptyOptionalWhenInvalidUserName() {
        Optional<User> result = repository.findByUsername(invalidUsername);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void verifyUsernameShouldReturnTrueWhenExistingUsername() {
        Assertions.assertTrue(repository.verifyUsername(validUsername));
    }

    @Test
    public void verifyUsernameShouldReturnFalseWhenNotExistingUsername() {
        Assertions.assertFalse(repository.verifyUsername(invalidUsername));
    }

    @Test
    public void saveShouldPersistUserWithAutoIncrementWhenIdIsNull() {
        User user = UserFactory.createUser();
        user.setId(null);
        User result = repository.save(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(countTotalProfessionals + 1, result.getId());
        Assertions.assertEquals("Jair Claudio", result.getName());
        Assertions.assertEquals("jair@gmail.com", result.getEmail());
        Assertions.assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    public void saveShouldUpdateProfessionalWhenValidId() {
        User user = UserFactory.createUser();
        user.setId(validId);
        User result = repository.save(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(validId, result.getId());
        Assertions.assertEquals("Jair Claudio", result.getName());
        Assertions.assertEquals("jair@gmail.com", result.getEmail());
        Assertions.assertEquals(Role.ADMIN, result.getRole());
    }
}

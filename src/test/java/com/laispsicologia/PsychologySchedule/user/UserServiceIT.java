package com.laispsicologia.PsychologySchedule.user;

import com.laispsicologia.PsychologySchedule.exceptions.AlreadyExistingUsernameException;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.UserFactory;
import com.laispsicologia.PsychologySchedule.user.dto.UserRegisterDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserReturnDTO;
import com.laispsicologia.PsychologySchedule.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@SpringBootTest
@Transactional
public class UserServiceIT {

    @Autowired
    private UserService service;

    private String existingUsername, nonExistingUsername;

    private String expectedUsername;
    private Collection<GrantedAuthority> expectedAuthorities;

    private UserRegisterDTO registerDto;

    @BeforeEach
    void setUp() throws Exception {
        existingUsername = "paulo@gmail.com";
        nonExistingUsername = "naoexiste@gmail.com";

        expectedUsername = "paulo@gmail.com";

        expectedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));

        registerDto = UserFactory.createUserRegisterDto();
    }

    @Test
    public void loadUserByUsernameShouldReturnOptionalNotEmptyWhenExistingUsername() {
        UserDetails result = service.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedUsername, result.getUsername());
        Assertions.assertEquals(expectedAuthorities, result.getAuthorities());
    }

    @Test
    public void loadUserByUsernameShouldThrowResourceNotFoundExceptionWhenNonExistingUsername() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingUsername);
        });
    }

    @Test
    public void registerUserShouldReturnUserReturnDtoWhenNonExistingUsername() {
        registerDto.setEmail(nonExistingUsername);

        UserReturnDTO result = service.registerUser(registerDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(registerDto.getName(), result.getName());
        Assertions.assertEquals(registerDto.getEmail(), result.getEmail());
        Assertions.assertEquals(registerDto.getRole(), result.getRole());


        UserDetails user = service.loadUserByUsername(result.getEmail());
        Assertions.assertNotEquals(registerDto.getPassword(), ((User) user).getPassword());
        Assertions.assertTrue(((User) user).getPassword().startsWith("$2"));
    }

    @Test
    public void registerUserShouldThrowAlreadyExistingUsernameExceptionWhenExistingUsername() {
        registerDto.setEmail(existingUsername);

        Assertions.assertThrows(AlreadyExistingUsernameException.class, () -> {
            service.registerUser(registerDto);
        });
    }
}

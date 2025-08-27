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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String existingUsername, nonExistingUsername;

    private User user;

    private UserRegisterDTO registerDto;

    @BeforeEach
    void setUp() throws Exception {
        existingUsername = "jair@gmail.com";
        nonExistingUsername = "naoexiste@gmail.com";

        user = UserFactory.createUser();

        registerDto = UserFactory.createUserRegisterDto();

        Mockito.when(repository.verifyUsername(existingUsername)).thenReturn(true);
        Mockito.when(repository.verifyUsername(nonExistingUsername)).thenReturn(false);

        Mockito.when(repository.findByUsername(existingUsername)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        Mockito.when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(passwordEncoder.encode(any())).thenReturn("Encoded password");
    }

    @Test
    public void loadUserByUsernameShouldReturnOptionalNotEmptyWhenExistingUsername() {
        UserDetails result = service.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getUsername(), result.getUsername());
        Assertions.assertEquals(user.getAuthorities(), result.getAuthorities());
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

        Mockito.verify(passwordEncoder).encode(registerDto.getPassword());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(registerDto.getName(), result.getName());
        Assertions.assertEquals(registerDto.getEmail(), result.getEmail());
        Assertions.assertEquals(registerDto.getRole(), result.getRole());
    }

    @Test
    public void registerUserShouldThrowAlreadyExistingUsernameExceptionWhenExistingUsername() {
        registerDto.setEmail(existingUsername);

        Assertions.assertThrows(AlreadyExistingUsernameException.class, () -> {
            service.registerUser(registerDto);
        });
    }
}

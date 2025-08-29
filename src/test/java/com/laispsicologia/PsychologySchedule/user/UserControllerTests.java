package com.laispsicologia.PsychologySchedule.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.config.security.SecurityFilter;
import com.laispsicologia.PsychologySchedule.config.security.token.TokenService;
import com.laispsicologia.PsychologySchedule.factory.UserFactory;
import com.laispsicologia.PsychologySchedule.user.dto.UserLoginDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserRegisterDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserReturnDTO;
import com.laispsicologia.PsychologySchedule.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

    private static final String BASE_URL = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private TokenService tokenService;

    private UserLoginDTO validLoginDto;
    private UsernamePasswordAuthenticationToken validUsernamePassword;
    private User user;
    private Authentication auth;
    private String validToken;
    private UserRegisterDTO registerDto;
    private UserReturnDTO returnDto;

    @BeforeEach
    void setUp() throws Exception {
        validLoginDto = UserFactory.createValidUserLoginDto();
        validUsernamePassword = new UsernamePasswordAuthenticationToken(validLoginDto.getEmail(),
                validLoginDto.getPassword());
        user = UserFactory.createUser();
        auth = new TestingAuthenticationToken(user, null, user.getAuthorities());
        validToken = "valid jwt token";

        registerDto = UserFactory.createUserRegisterDto();
        returnDto = UserFactory.createUserReturnDto();

        Mockito.when(authenticationManager.authenticate(eq(validUsernamePassword))).thenReturn(auth);

        Mockito.when(tokenService.generatedToken((User) auth.getPrincipal())).thenReturn(validToken);

        Mockito.when(service.registerUser(any(UserRegisterDTO.class))).thenReturn(returnDto);
    }

    @Test
    public void loginShouldReturnValidTokenWhenValidUserLoginDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validLoginDto);

        mockMvc.perform(post(BASE_URL + "/login")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(validToken));
    }

    @Test
    public void registerShouldReturnUserReturnDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(registerDto.getName()))
                .andExpect(jsonPath("$.email").value(registerDto.getEmail()))
                .andExpect((jsonPath("$.role")).value(registerDto.getRole().toString()));
    }
}

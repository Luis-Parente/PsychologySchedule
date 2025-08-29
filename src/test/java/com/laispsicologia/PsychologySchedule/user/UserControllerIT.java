package com.laispsicologia.PsychologySchedule.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.factory.UserFactory;
import com.laispsicologia.PsychologySchedule.user.dto.UserLoginDTO;
import com.laispsicologia.PsychologySchedule.user.dto.UserRegisterDTO;
import com.laispsicologia.PsychologySchedule.utils.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    private static final String BASE_URL = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String email, password, bearerToken;

    private UserLoginDTO validLoginDto;

    private UserRegisterDTO registerDto;

    @BeforeEach
    void setUp() throws Exception {
        email = "paulo@gmail.com";
        password = "123456789";
        validLoginDto = new UserLoginDTO(email, password);
        registerDto = UserFactory.createUserRegisterDto();

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, email, password);
    }

    @Test
    public void loginShouldReturnValidTokenWhenValidUserLoginDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validLoginDto);

        mockMvc.perform(post(BASE_URL + "/login")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void registerShouldReturnUserReturnDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(registerDto);

        mockMvc.perform(post(BASE_URL + "/register")
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(registerDto.getName()))
                .andExpect(jsonPath("$.email").value(registerDto.getEmail()))
                .andExpect((jsonPath("$.role")).value(registerDto.getRole().toString()));
    }
}

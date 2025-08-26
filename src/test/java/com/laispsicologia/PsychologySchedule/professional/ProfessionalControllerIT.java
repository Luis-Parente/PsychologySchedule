package com.laispsicologia.PsychologySchedule.professional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.factory.ProfessionalFactory;
import com.laispsicologia.PsychologySchedule.professional.dto.ProfessionalDTO;
import com.laispsicologia.PsychologySchedule.utils.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProfessionalControllerIT {

    private static final String BASE_URL = "/professionals";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private Long validId, invalidId;

    private ProfessionalDTO dto;

    private String expectedName, expectedRegistrationNumber, expectedEmail, expectedPhoneNumber;

    private String email, password, bearerToken;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        dto = ProfessionalFactory.createProfessionalDTO();

        expectedName = "Mauro Silva";
        expectedRegistrationNumber = "CRP SP/123456";
        expectedEmail = "mauro@gmail.com";
        expectedPhoneNumber = "91234-1234";

        email = "paulo@gmail.com";
        password = "123456789";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, email, password);
    }

    @Test
    public void findAllShouldReturnPageOfProfessionalDto() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProfessionalDtoWhenValidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.registrationNumber").value(expectedRegistrationNumber))
                .andExpect(jsonPath("$.email").value(expectedEmail))
                .andExpect(jsonPath("$.phoneNumber").value(expectedPhoneNumber));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProfessionalDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.registrationNumber").value(dto.getRegistrationNumber()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(dto.getPhoneNumber()));
    }

    @Test
    public void updateShouldReturnProfessionalDtoWhenValidId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.registrationNumber").value(dto.getRegistrationNumber()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(dto.getPhoneNumber()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalidId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenValidId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNoContentWhenInalidId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

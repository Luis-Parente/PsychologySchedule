package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.emergencyContact.entity.Relationship;
import com.laispsicologia.PsychologySchedule.factory.EmergencyContactFactory;
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
public class EmergencyContactControllerIT {

    private static final String BASE_URL = "/contacts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private Long validId, invalidId;

    private Long invalidClientId;

    private EmergencyContactDTO dto;

    private String email, password, bearerToken;

    private String expectedName, expectedEmail, expectedPhoneNumber, expectedRelationship;

    private Long expectedClientId;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        invalidClientId = 1000L;

        dto = EmergencyContactFactory.createEmergencyContactDto();

        email = "paulo@gmail.com";
        password = "123456789";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, email, password);

        expectedName = "Douglas Souza";
        expectedEmail = "douglas@gmail.com";
        expectedPhoneNumber = "93214-3214";
        expectedRelationship = Relationship.SIBLING.toString();
        expectedClientId = 1L;
    }

    @Test
    public void findAllShouldReturnPageOfEmergencyContactDto() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnEmergencyContactDtoWhenValidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.email").value(expectedEmail))
                .andExpect(jsonPath("$.phoneNumber").value(expectedPhoneNumber))
                .andExpect(jsonPath("$.relationship").value(expectedRelationship))
                .andExpect(jsonPath("$.clientId").value(expectedClientId));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnEmergencyContactDtoWhenValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(dto.getPhoneNumber()))
                .andExpect(jsonPath("$.relationship").value(dto.getRelationship()))
                .andExpect(jsonPath("$.clientId").value(dto.getClientId()));
    }

    @Test
    public void insertShouldReturnNotFoundWhenInvalidClientId() throws Exception {
        dto.setClientId(invalidClientId);
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnEmergencyContactDtoWhenValidEmergencyContactIdAndValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(dto.getPhoneNumber()))
                .andExpect(jsonPath("$.relationship").value(dto.getRelationship()))
                .andExpect(jsonPath("$.clientId").value(dto.getClientId()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalidEmergencyContactId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", invalidId)
                        .header("Authorization", bearerToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalidClientId() throws Exception {
        dto.setClientId(invalidClientId);
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId)
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

package com.laispsicologia.PsychologySchedule.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.testutils.ClientFactory;
import com.laispsicologia.PsychologySchedule.utils.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClientControllerIT {

    private static final String BASE_URL = "/clients";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingId, nonExistingId;

    private String email, password, bearerToken;

    private ClientDTO clientDTO;

    private String name;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;

        email = "paulo@gmail.com";
        password = "123456789";

        clientDTO = ClientFactory.createClientDto();

        name = "Nelson";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, email, password);
    }

    @Test
    public void findAllShouldReturnPageOfClients() throws Exception {
        mockMvc.perform(
                        get(BASE_URL).header("Authorization", bearerToken).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByNameShouldReturnPageOfClientsFilteredByName() throws Exception {
        mockMvc.perform(get(BASE_URL + "/findByName").param("name", name).header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnClientDtoWhenIdExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", existingId).header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", nonExistingId).header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void insertShouldReturnClientDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(
                post(BASE_URL).header("Authorization", bearerToken).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(clientDTO.getName()));
    }

    @Test
    public void updateShouldReturnClientDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(
                put(BASE_URL + "/{id}", existingId).header("Authorization", bearerToken).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(clientDTO.getName()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(
                put(BASE_URL + "/{id}", nonExistingId).header("Authorization", bearerToken).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldRetunNoContentWhenValidId() throws Exception {
        ResultActions result = mockMvc.perform(
                delete(BASE_URL + "/{id}", existingId).header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc
                .perform(delete(BASE_URL + "/{id}", nonExistingId).header("Authorization", bearerToken)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}

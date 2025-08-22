package com.laispsicologia.PsychologySchedule.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.client.dto.ClientDTO;
import com.laispsicologia.PsychologySchedule.client.dto.ClientMinDTO;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.ClientFactory;
import com.laispsicologia.PsychologySchedule.security.SecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService service;

    private ClientDTO clientDTO;
    private ClientMinDTO clientMinDTO;
    private PageImpl<ClientMinDTO> clientPage;

    private String name;

    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() {
        clientDTO = ClientFactory.createClientDto();
        clientMinDTO = ClientFactory.createClientMinDto();

        clientPage = new PageImpl<>(List.of(clientMinDTO));

        name = "Nelson";

        existingId = 1L;
        nonExistingId = 1000L;

        when(service.findAll(any())).thenReturn((clientPage));

        when(service.findByName(eq(name), any())).thenReturn(clientPage);

        when(service.findById(eq(existingId))).thenReturn(clientDTO);
        when(service.findById(eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(clientDTO);

        when(service.update(eq(existingId), any())).thenReturn(clientDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
    }

    @Test
    public void findAllShouldReturnPageOfClients() throws Exception {
        mockMvc.perform(get("/clients").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void findByNameShouldReturnPageOfClientsFilteredByName() throws Exception {
        mockMvc.perform(get("/clients/findByName").param("name", name).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnClientDtoWhenIdExists() throws Exception {
        mockMvc.perform(get("/clients/{id}", existingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientDTO.getId()))
                .andExpect(jsonPath("$.name").value(clientDTO.getName()));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(get("/clients/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnClientDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(post("/clients").content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(clientDTO.getName()));
    }

    @Test
    public void updateShouldReturnClientDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(put("/clients/{id}", existingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(clientDTO.getName()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(clientDTO);

        ResultActions result = mockMvc.perform(put("/clients/{id}", nonExistingId).content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldRetunNoContentWhenValidId() throws Exception {
        ResultActions result = mockMvc.perform(delete("/clients/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc
                .perform(delete("/clients/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}

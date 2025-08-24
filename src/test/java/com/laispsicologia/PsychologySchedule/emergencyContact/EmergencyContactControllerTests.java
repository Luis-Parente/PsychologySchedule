package com.laispsicologia.PsychologySchedule.emergencyContact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.config.security.SecurityFilter;
import com.laispsicologia.PsychologySchedule.emergencyContact.dto.EmergencyContactDTO;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.EmergencyContactFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmergencyContactController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class EmergencyContactControllerTests {

    private static final String BASE_URL = "/contacts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmergencyContactService service;

    private Long validId, invalidId;

    private Long invalidClientId;

    private PageImpl<EmergencyContactDTO> page;

    private EmergencyContactDTO dto;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        invalidClientId = 1000L;

        dto = EmergencyContactFactory.createEmergencyContactDto();

        page = new PageImpl<>(List.of(dto));

        Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

        Mockito.when(service.findById(eq(validId))).thenReturn(dto);
        Mockito.when(service.findById(eq(invalidId))).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insert(any(EmergencyContactDTO.class))).thenReturn(dto);

        Mockito.when(service.insert(Mockito.argThat(dto -> {
            return dto != null && dto.getClientId().equals(invalidClientId);
        }))).thenThrow((ResourceNotFoundException.class));

        Mockito.when(service.update(eq(validId), any(EmergencyContactDTO.class))).thenReturn(dto);

        Mockito.when(service.update(eq(invalidId), any(EmergencyContactDTO.class)))
                .thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(eq(validId), Mockito.argThat(dto -> {
            return dto != null && dto.getClientId().equals(invalidClientId);
        }))).thenThrow((ResourceNotFoundException.class));

        Mockito.doNothing().when(service).delete(validId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(invalidId);
    }

    @Test
    public void findAllShouldReturnPageOfEmergencyContactDto() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnEmergencyContactDtoWhenValidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(dto.getPhoneNumber()))
                .andExpect(jsonPath("$.relationship").value(dto.getRelationship()))
                .andExpect(jsonPath("$.clientId").value(dto.getClientId()));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnEmergencyContactDtoWhenValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(BASE_URL)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(validId))
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

        mockMvc.perform(post(BASE_URL).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnEmergencyContactDtoWhenValidEmergencyContactIdAndValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId).content(jsonBody)
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
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenValidId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNoContentWhenInalidId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

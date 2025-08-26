package com.laispsicologia.PsychologySchedule.professional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.config.security.SecurityFilter;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.ProfessionalFactory;
import com.laispsicologia.PsychologySchedule.professional.dto.ProfessionalDTO;
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

@WebMvcTest(controllers = ProfessionalController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class ProfessionalControllerTests {

    private static final String BASE_URL = "/professionals";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProfessionalService service;

    private Long validId, invalidId;

    private PageImpl<ProfessionalDTO> page;

    private ProfessionalDTO dto;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        dto = ProfessionalFactory.createProfessionalDTO();

        page = new PageImpl<>(List.of(dto));

        Mockito.when(service.findAll(any(Pageable.class))).thenReturn(page);

        Mockito.when(service.findById(eq(validId))).thenReturn(dto);
        Mockito.when(service.findById(eq(invalidId))).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insert(any(ProfessionalDTO.class))).thenReturn(dto);

        Mockito.when(service.update(eq(validId), any(ProfessionalDTO.class))).thenReturn(dto);

        Mockito.when(service.update(eq(invalidId), any(ProfessionalDTO.class)))
                .thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(service).delete(validId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(invalidId);
    }

    @Test
    public void findAllShouldReturnPageOfProfessionalDto() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProfessionalDtoWhenValidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validId))
                .andExpect(jsonPath("$.name").value(dto.getName()))
                .andExpect(jsonPath("$.registrationNumber").value(dto.getRegistrationNumber()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(dto.getPhoneNumber()));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProfessionalDto() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(BASE_URL)
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

        mockMvc.perform(put(BASE_URL + "/{id}", validId).content(jsonBody)
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

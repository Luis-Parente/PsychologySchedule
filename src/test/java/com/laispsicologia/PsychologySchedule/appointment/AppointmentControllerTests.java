package com.laispsicologia.PsychologySchedule.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentDTO;
import com.laispsicologia.PsychologySchedule.appointment.dto.AppointmentMinDTO;
import com.laispsicologia.PsychologySchedule.exceptions.InvalidDateException;
import com.laispsicologia.PsychologySchedule.exceptions.ResourceNotFoundException;
import com.laispsicologia.PsychologySchedule.factory.AppointmentFactory;
import com.laispsicologia.PsychologySchedule.security.SecurityFilter;
import org.hamcrest.Matchers;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AppointmentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class AppointmentControllerTests {

    private static final String BASE_URL = "/appointments";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService service;

    private Long validId, invalidId;

    private Long invalidClientId;

    private LocalDateTime invalidDate;

    private PageImpl<AppointmentDTO> page;

    private AppointmentDTO dto;

    private AppointmentMinDTO minDto;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 1000L;

        invalidClientId = 1000L;

        invalidDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS);

        dto = AppointmentFactory.createAppointmentDto();

        minDto = AppointmentFactory.createAppointmentMinDto();

        page = new PageImpl<>(List.of(dto));

        Mockito.when(service.findFilteredByDate(any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(page);

        Mockito.when(service.findById(eq(validId))).thenReturn(dto);
        Mockito.when(service.findById(eq(invalidId))).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insert(any(AppointmentMinDTO.class))).thenReturn(dto);

        Mockito.when(service.insert(Mockito.argThat(minDto -> {
            return minDto != null && minDto.getStartTime().equals(invalidDate);
        }))).thenThrow((InvalidDateException.class));

        Mockito.when(service.insert(Mockito.argThat(minDto -> {
            return minDto != null && minDto.getClientId().equals(invalidClientId);
        }))).thenThrow((ResourceNotFoundException.class));

        Mockito.when(service.update(eq(validId), any(AppointmentDTO.class))).thenReturn(dto);

        Mockito.when(service.update(eq(invalidId), any(AppointmentDTO.class)))
                .thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(eq(validId), Mockito.argThat(dto -> {
            return dto != null && dto.getClientId().equals(invalidClientId);
        }))).thenThrow((ResourceNotFoundException.class));

        Mockito.doNothing().when(service).delete(validId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(invalidId);
    }

    @Test
    public void findFilteredByDateShouldReturnPageOfAppointment() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("firstDate", String.valueOf(LocalDate.now().minusDays(15)))
                        .param("lastDate", String.valueOf(LocalDate.now().plusDays(15)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnAppointmentDtoWhenValidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", validId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.startTime").value(
                        Matchers.startsWith(dto.getStartTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.endTime").value(
                        Matchers.startsWith(dto.getEndTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.appointmentStatus").value(dto.getAppointmentStatus()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()))
                .andExpect(jsonPath("$.paid").value(dto.getPaid()))
                .andExpect(jsonPath("$.clientId").value(dto.getClientId()))
                .andExpect(jsonPath("$.clientName").value(dto.getClientName()));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnAppointmentDtoWhenValidDateAndValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.startTime").value(
                        Matchers.startsWith(dto.getStartTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.endTime").value(
                        Matchers.startsWith(dto.getEndTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.appointmentStatus").value(dto.getAppointmentStatus()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()))
                .andExpect(jsonPath("$.paid").value(dto.getPaid()))
                .andExpect(jsonPath("$.clientId").value(dto.getClientId()))
                .andExpect(jsonPath("$.clientName").value(dto.getClientName()));
    }

    @Test
    public void insertShouldReturnNotFoundWhenValidDateAndInvalidClientId() throws Exception {
        minDto.setClientId(invalidClientId);
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnConflictWhenInvalidDateAndValidClientId() throws Exception {
        minDto.setStartTime(invalidDate);
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenStartTimeIsPast() throws Exception {
        minDto.setStartTime(LocalDateTime.now().minusDays(1));
        String jsonBody = objectMapper.writeValueAsString(minDto);

        mockMvc.perform(post(BASE_URL)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].fieldName").value("startTime"))
                .andExpect(jsonPath("$.errors[0].message").value("Date must be a future date"));
    }

    @Test
    public void updateShouldReturnAppointmentDtoWhenValidAppointmentIdAndValidClientId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(BASE_URL + "/{id}", validId).content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.startTime").value(
                        Matchers.startsWith(dto.getStartTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.endTime").value(
                        Matchers.startsWith(dto.getEndTime().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.appointmentStatus").value(dto.getAppointmentStatus()))
                .andExpect(jsonPath("$.price").value(dto.getPrice()))
                .andExpect(jsonPath("$.paid").value(dto.getPaid()))
                .andExpect(jsonPath("$.clientId").value(dto.getClientId()))
                .andExpect(jsonPath("$.clientName").value(dto.getClientName()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalidAppointmentId() throws Exception {
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

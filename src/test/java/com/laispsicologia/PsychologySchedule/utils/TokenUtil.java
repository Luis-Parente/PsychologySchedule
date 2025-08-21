package com.laispsicologia.PsychologySchedule.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laispsicologia.PsychologySchedule.user.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TokenUtil {

    @Autowired
    private ObjectMapper objectMapper;

    private UserLoginDTO loginDTO;

    public String obtainAccessToken(MockMvc mockMvc, String email, String password) throws Exception {
        loginDTO = new UserLoginDTO(email, password);

        String jsonBody = objectMapper.writeValueAsString(loginDTO);

        ResultActions result = mockMvc
                .perform(post("/users/login")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(content().contentType("application/json"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return "Bearer " + jsonParser.parseMap(resultString).get("token").toString();
    }
}

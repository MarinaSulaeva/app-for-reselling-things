package ru.skypro.homework;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.skypro.homework.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void getInformation_status_isOk() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }

    @Test
    public void getInformation_status_throw401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void setPassword_status_isOk() throws Exception {
        JSONObject newPassword = new JSONObject();
        newPassword.put("currentPassword", "password");
        newPassword.put("newPassword", "password1");
        mockMvc.perform(post("/users/set-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword.toString()))
                .andExpect(status().isOk());
    }




}

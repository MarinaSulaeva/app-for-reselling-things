package ru.skypro.homework.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository usersRepository;

    private PasswordEncoder passwordEncoder;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("postgres")
            .withPassword("73aberiv");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private String base64Encoded(String login, String password) {
        return Base64Utils.encodeToString((login + ":" + password).getBytes(StandardCharsets.UTF_8));
    }


    private void addToDb() {
        usersRepository.deleteAll();
        Users user = new Users(1,
                "D:\\userImage\\маринасулаева.png",
                "user@gmail.com",
                "$2a$10$mShIMZIKnJ.EVqUycC2OE.qunAUqKJPFZq6ADSuJ.IYmVWBmXqWMi",
                "ivan",
                "ivanov",
                "+7 777-77-77",
                Role.USER,
                new ArrayList<>());
        usersRepository.save(user);
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void getInformation_status_isOk() throws Exception {
        addToDb();
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "USER", password = "password1")
    public void getInformation_status_isUserNotFound() throws Exception {
        addToDb();
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getInformation_status_throw401() throws Exception {
        addToDb();
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void setPassword_status_isOk() throws Exception {
        addToDb();
        JSONObject newPassword = new JSONObject();
        newPassword.put("currentPassword", "password");
        newPassword.put("newPassword", "password1");
        mockMvc.perform(post("/users/set_password")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded("user@gmail.com", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void setPassword_status_isForbidden() throws Exception {
        addToDb();
        JSONObject newPassword = new JSONObject();
        newPassword.put("currentPassword", "password1");
        newPassword.put("newPassword", "password2");
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void setPassword_status_isUnauthorized() throws Exception {
        addToDb();
        JSONObject newPassword = new JSONObject();
        newPassword.put("currentPassword", "password1");
        newPassword.put("newPassword", "password2");
        mockMvc.perform(post("/users/set-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateInformationAboutUser_status_isUnauthorized() throws Exception {
        addToDb();
        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "ivan");
        updateUser.put("lastName", "ivanova");
        updateUser.put("phone", "+7(777)-777-77-77");
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUser.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void updateInformationAboutUser_status_isOk() throws Exception {
        addToDb();
        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "ivan");
        updateUser.put("lastName", "ivanova");
        updateUser.put("phone", "+7(777)-777-77-77");
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUser.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void updateImage_status_isOk() throws Exception {
        addToDb();
        String name = "user-icon.png";
        byte[] content = Files.readAllBytes(Paths.get("user-icon.png"));
        MultipartFile result = new MockMultipartFile(name, content);
        mockMvc.perform(patch("/users/me/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(result.getBytes()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateImage_status_isUnauthorized() throws Exception {
        addToDb();
        String name = "user-icon.png";
        byte[] content = Files.readAllBytes(Paths.get("user-icon.png"));
        MultipartFile result = new MockMultipartFile(name, content);
        mockMvc.perform(patch("/users/me/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(result.getBytes()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void updateImage_status_isNotFound() throws Exception {
        addToDb();
        String name = "user-icon.tif";
        byte[] content = Files.readAllBytes(Paths.get("user-icon.tif"));
        MultipartFile result = new MockMultipartFile(name, content);
        mockMvc.perform(patch("/users/me/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(result.getBytes()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void getImage_status_isOk() throws Exception {
        usersRepository.deleteAll();
        addToDb();
        MvcResult result = mockMvc.perform(get("/users/me/image"))
                .andExpect(status().isOk())
                .andReturn();
        byte[] resourceContent = result.getResponse().getContentAsByteArray();
        assertThat(resourceContent).isNotEmpty();
    }
}
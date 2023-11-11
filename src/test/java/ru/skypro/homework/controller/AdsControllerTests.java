package ru.skypro.homework.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.ImageAd;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.ImageAdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AdsService;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AdsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdsService adsService;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private ImageAdRepository imageAdRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ImageRepository imageRepository;

//    @Container
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
//            .withUsername("postgres")
//            .withPassword("Anna_098!");

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withUsername("postgres")
            .withPassword("73aberiv");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void cleanData() {
        adsRepository.deleteAll();
        usersRepository.deleteAll();
        imageAdRepository.deleteAll();
    }

    @Test
    void testPostgresql() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            assertThat(conn).isNotNull();
        }
    }


    private void addEntityToDatabase() throws IOException {
        Users user = usersRepository.save(new Users(1,
                null,
                "user@gmail.com",
                "$2a$10$mShIMZIKnJ.EVqUycC2OE.qunAUqKJPFZq6ADSuJ.IYmVWBmXqWMi",
                "ivan",
                "ivanov",
                "+7 777-77-77",
                Role.USER));

        Ad ad = adsRepository.save(new Ad(
                1,
                "product1",
                "interesting product",
                100,
                null,
                user,
                new ArrayList<>()));
        ImageAd imageAd = new ImageAd();
        imageAd.setImage(Files.readAllBytes(Paths.get("imageAd_test/imageAd.png")));
        imageAd.setId(ad.getPk().toString());
        imageAdRepository.save(imageAd);
        ad.setImage(imageAd);
        adsRepository.save(ad);

        Ad ad1 = adsRepository.save(new Ad(
                2,
                "product2",
                "very interesting product",
                1000,
                null,
                user,
                new ArrayList<>()));
        ImageAd imageAd1 = new ImageAd();
        imageAd1.setImage(Files.readAllBytes(Paths.get("imageAd_test/imageAd1.png")));
        imageAd1.setId(ad1.getPk().toString());
        imageAdRepository.save(imageAd1);
        ad1.setImage(imageAd1);
        adsRepository.save(ad1);
    }

    private void addUserInRepository() {
        Users user = usersRepository.save(new Users(2,
                null,
                "user1@gmail.com",
                "$2a$10$mShIMZIKnJ.EVqUycC2OE.qunAUqKJPFZq6ADSuJ.IYmVWBmXqWMi",
                "ivan",
                "ivanov",
                "+7 777-77-77",
                Role.USER));
    }

    private void addAdminInRepository() {
        Users user = usersRepository.save(new Users(2,
                null,
                "admin@gmail.com",
                "$2a$10$mShIMZIKnJ.EVqUycC2OE.qunAUqKJPFZq6ADSuJ.IYmVWBmXqWMi",
                "ivan",
                "ivanov",
                "+7 777-77-77",
                Role.ADMIN));
    }

    private JSONObject createJSONObjectUpdateAd() throws JSONException {
        JSONObject updateAd = new JSONObject();
        updateAd.put("title", "product10");
        updateAd.put("price", 300000);
        updateAd.put("description", "very useful for you");
        return updateAd;
    }

    private JSONObject createJSONObjectAdNotValid() throws JSONException {
        JSONObject updateAd = new JSONObject();
        updateAd.put("title", "pro");
        updateAd.put("price", 100000000);
        updateAd.put("description", "useful");
        return updateAd;
    }


    @Test
    void getAllAdsTest_OK() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.results").isEmpty());

        addEntityToDatabase();

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void addAdTest_IsNotValid() throws Exception {

        addEntityToDatabase();

        JSONObject newAd = createJSONObjectAdNotValid();
        String json = newAd.toString();

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        MockPart properties = new MockPart("properties", json.getBytes());
        properties.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        int id = usersRepository.findByUsername("user@gmail.com").get().getId();

        mockMvc.perform(multipart("/ads")
                        .part(mockPart, properties))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void addAdTest_OK() throws Exception {

        addEntityToDatabase();

        JSONObject newAd = createJSONObjectUpdateAd();
        String json = newAd.toString();

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        MockPart properties = new MockPart("properties", json.getBytes());
        properties.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        int id = usersRepository.findByUsername("user@gmail.com").get().getId();

        mockMvc.perform(multipart("/ads")
                        .part(mockPart, properties))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.author").value(id));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void getAdsTest_OK() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        mockMvc.perform(get("/ads/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorFirstName").value("ivan"))
                .andExpect(jsonPath("$.price").value("100"));
    }

    @Test
    void getAdsTest_Unauthorized() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        mockMvc.perform(get("/ads/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void getAdsTest_isNotFound() throws Exception {
        addEntityToDatabase();
        int id = 1000;
        mockMvc.perform(get("/ads/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void removeAdTest_isNoContent() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        mockMvc.perform(delete("/ads/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeAdTest_Unauthorized() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        mockMvc.perform(delete("/ads/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user1@gmail.com", roles = "USER", password = "password")
    void removeAdTest_Forbidden() throws Exception {
        addEntityToDatabase();
        addUserInRepository();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        mockMvc.perform(delete("/ads/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void removeAdTest_isNotFound() throws Exception {
        addEntityToDatabase();
        int id = 1000;
        mockMvc.perform(delete("/ads/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN", password = "password")
    void removeAdTest_isNoContent_Admin() throws Exception {
        addEntityToDatabase();
        addAdminInRepository();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        mockMvc.perform(delete("/ads/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void updateAdsTest_Ok() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        JSONObject updateAd = createJSONObjectUpdateAd();
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAd.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/ads/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("product10"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    public void updateAdsTest_isNotValid() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        JSONObject updateAd = createJSONObjectAdNotValid();

        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAd.toString()))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/ads/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("product1"));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN", password = "password")
    void updateAdsTest_Ok_Admin() throws Exception {
        addEntityToDatabase();
        addAdminInRepository();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        JSONObject updateAd = createJSONObjectUpdateAd();
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAd.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1@gmail.com", roles = "USER", password = "password")
    void updateAdsTest_Forbidden() throws Exception {
        addEntityToDatabase();
        addUserInRepository();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        JSONObject updateAd = createJSONObjectUpdateAd();
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAd.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateAdsTest_Unauthorized() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();
        JSONObject updateAd = createJSONObjectUpdateAd();
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAd.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void updateAdsTest_isNotFound() throws Exception {
        addEntityToDatabase();
        int id = 1000;
        JSONObject updateAd = createJSONObjectUpdateAd();
        mockMvc.perform(patch("/ads/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAd.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void getAdsMeTest_Ok() throws Exception {
        addEntityToDatabase();
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results").isNotEmpty());
    }

    @Test
    void getAdsMeTest_Unauthorized() throws Exception {
        addEntityToDatabase();
        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void updateImageTest_Ok() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .part(mockPart)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with((request -> {
                            request.setMethod("PATCH");
                            return request;
                        })))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN", password = "password")
    void updateImageTest_Ok_Admin() throws Exception {
        addEntityToDatabase();
        addAdminInRepository();
        int id = adsRepository.findAdByTitle("product1").get().getPk();

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .part(mockPart)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with((request -> {
                            request.setMethod("PATCH");
                            return request;
                        })))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1@gmail.com", roles = "USER", password = "password")
    void updateImageTest_Ok_Forbidden() throws Exception {
        addEntityToDatabase();
        addUserInRepository();
        int id = adsRepository.findAdByTitle("product1").get().getPk();

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .part(mockPart)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with((request -> {
                            request.setMethod("PATCH");
                            return request;
                        })))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateImageTest_Unauthorized() throws Exception {
        addEntityToDatabase();
        int id = adsRepository.findAdByTitle("product1").get().getPk();

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .part(mockPart)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with((request -> {
                            request.setMethod("PATCH");
                            return request;
                        })))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void updateImageTest_Not_Found() throws Exception {
        addEntityToDatabase();
        int id = 1000;

        ClassPathResource classPathResource = new ClassPathResource("imageAd_test.png");
        MockPart mockPart = new MockPart("image", "imageAd_test.png", classPathResource.getInputStream().readAllBytes());
        mockPart.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        mockMvc.perform(multipart("/ads/{id}/image", id)
                        .part(mockPart)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with((request -> {
                            request.setMethod("PATCH");
                            return request;
                        })))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@gmail.com", roles = "USER", password = "password")
    void getImageTest() throws Exception {
        addEntityToDatabase();
        Ad ad = adsRepository.findAdByTitle("product1").get();

        MvcResult result = mockMvc.perform(get("/ads/{id}/image", ad.getPk()))
                .andExpect(status().isOk())
                .andReturn();

        byte[] resourceContent = result.getResponse().getContentAsByteArray();
        assertThat(resourceContent).isNotEmpty();
    }

}

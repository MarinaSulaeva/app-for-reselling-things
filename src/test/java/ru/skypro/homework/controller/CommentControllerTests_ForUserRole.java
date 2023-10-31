package ru.skypro.homework.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;
import ru.skypro.homework.TestConstants;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.CommentService;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.TestConstants.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

@Testcontainers
public class CommentControllerTests_ForUserRole {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AdsRepository adsRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    public String autorisationUser(String username, String passwordDecode) {
        return Base64Utils.encodeToString((username + ":" + passwordDecode).getBytes(StandardCharsets.UTF_8));
    }

    @BeforeEach
    public void fillRepository() {
        Users admin = new Users();
        admin.setRole(ROLE_ADMIN);
//        admin.setImage(IMAGE_ADMIN);
        admin.setUsername(USERNAME_ADMIN);
        admin.setPassword(PASSWORD_ADMIN_ENCODED);
        admin.setFirstName(FIRST_NAME_ADMIN);
        admin.setLastName(LAST_NAME_ADMIN);
        admin.setPhone(PHONE_ADMIN);
        usersRepository.save(admin);

        Users user1 = new Users();
        user1.setRole(ROLE_USER);
//        user1.setImage(IMAGE_USER1);
        user1.setUsername(USERNAME_USER1);
        user1.setPassword(PASSWORD_USER1_ENCODED);
        user1.setFirstName(FIRST_NAME_USER1);
        user1.setLastName(LAST_NAME_USER1);
        user1.setPhone(PHONE_USER1);
        usersRepository.save(user1);

        Users user2 = new Users();
        user2.setRole(ROLE_USER);
//        user2.setImage(IMAGE_USER2);
        user2.setUsername(USERNAME_USER2);
        user2.setPassword(PASSWORD_USER2_ENCODED);
        user2.setFirstName(FIRST_NAME_USER2);
        user2.setLastName(LAST_NAME_USER2);
        user2.setPhone(PHONE_USER2);
        usersRepository.save(user2);

        Ad ad1User1 = new Ad();
        ad1User1.setTitle(TITLE_AD1);
        ad1User1.setDescription(DESCRIPTION1);
        ad1User1.setPrice(PRICE_AD1);
//        ad1User1.setImage(IMAGE_AD1);
        ad1User1.setUser(usersRepository.findByUsername(USERNAME_USER1).orElse(user1));
        adsRepository.save(ad1User1);

        Comment comment1Ad1User2 = new Comment();
        comment1Ad1User2.setUsers(usersRepository.findByUsername(USERNAME_USER2).orElse(user2));
        comment1Ad1User2.setAd(adsRepository.findAdByPk(1).orElse(ad1User1));
        comment1Ad1User2.setCreatedAt(CREATED_AT_COMMENT1_FOR_AD1);
        comment1Ad1User2.setText(COMMENT1_FOR_AD_1);
        commentRepository.save(comment1Ad1User2);

        Comment comment2Ad1User2 = new Comment();
        comment2Ad1User2.setUsers(usersRepository.findByUsername(USERNAME_USER2).orElse(user2));
        comment2Ad1User2.setAd(adsRepository.findAdByPk(1).orElse(ad1User1));
        comment2Ad1User2.setCreatedAt(CREATED_AT_COMMENT2_FOR_AD1);
        comment2Ad1User2.setText(COMMENT2_FOR_AD_1);
        commentRepository.save(comment2Ad1User2);

        Comment comment3Ad1User1 = new Comment();
        comment3Ad1User1.setUsers(usersRepository.findByUsername(USERNAME_USER1).orElse(user1));
        comment3Ad1User1.setAd(adsRepository.findAdByPk(1).orElse(ad1User1));
        comment3Ad1User1.setCreatedAt(CREATED_AT_COMMENT3_FOR_AD1);
        comment3Ad1User1.setText(COMMENT3_FOR_AD_1);
        commentRepository.save(comment3Ad1User1);

        Ad ad2User2 = new Ad();
        ad2User2.setTitle(TITLE_AD2);
        ad2User2.setDescription(DESCRIPTION2);
        ad2User2.setPrice(PRICE_AD2);
//        ad2User2.setImage(IMAGE_AD2);
        ad2User2.setUser(usersRepository.findByUsername(USERNAME_USER2).orElse(user2));
        adsRepository.save(ad2User2);

        Comment comment4Ad2User1 = new Comment();
        comment4Ad2User1.setUsers(usersRepository.findByUsername(USERNAME_USER1).orElse(user1));
        comment4Ad2User1.setAd(adsRepository.findAdByPk(2).orElse(ad2User2));
        comment4Ad2User1.setCreatedAt(CREATED_AT_COMMENT4_FOR_AD2);
        comment4Ad2User1.setText(COMMENT4_FOR_AD_2);
        commentRepository.save(comment4Ad2User1);

        Comment comment5Ad2User1 = new Comment();
        comment5Ad2User1.setUsers(usersRepository.findByUsername(USERNAME_USER1).orElse(user1));
        comment5Ad2User1.setAd(adsRepository.findAdByPk(2).orElse(ad2User2));
        comment5Ad2User1.setCreatedAt(CREATED_AT_COMMENT5_FOR_AD2);
        comment5Ad2User1.setText(COMMENT5_FOR_AD_2);
        commentRepository.save(comment5Ad2User1);

        Comment comment6Ad2User1 = new Comment();
        comment6Ad2User1.setUsers(usersRepository.findByUsername(USERNAME_USER1).orElse(user1));
        comment6Ad2User1.setAd(adsRepository.findAdByPk(2).orElse(ad2User2));
        comment6Ad2User1.setCreatedAt(CREATED_AT_COMMENT6_FOR_AD2);
        comment6Ad2User1.setText(COMMENT6_FOR_AD_2);
        commentRepository.save(comment6Ad2User1);

        Comment comment7Ad2Admin = new Comment();
        comment7Ad2Admin.setUsers(usersRepository.findByUsername(USERNAME_ADMIN).orElse(admin));
        comment7Ad2Admin.setAd(adsRepository.findAdByPk(2).orElse(ad2User2));
        comment7Ad2Admin.setCreatedAt(CREATED_AT_COMMENT7_FOR_AD2);
        comment7Ad2Admin.setText(COMMENT7_FOR_AD_2);
        commentRepository.save(comment7Ad2Admin);

    }

    @AfterEach
    public void cleanData() {
        adsRepository.deleteAll();
        commentRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    void getComments() throws Exception {
        String base64Encoded = autorisationUser(USERNAME_USER2, PASSWORD_USER2);

        int adId2 = commentRepository.findLastAdId();
        int countCommentByAd2 = commentRepository.countCommentsByAdId(adId2);

        //получение комментариев по существующему объявлению
        mockMvc.perform(get("/ads/{id}/comments", adId2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(countCommentByAd2));

        //получение комментариев по несуществующему объявлению
        mockMvc.perform(get("/ads/{id}/comments", adId2+1)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void addComment() throws Exception {
        String base64Encoded = autorisationUser(USERNAME_USER2, PASSWORD_USER2);

        JSONObject newComment = new JSONObject();
        newComment.put("text", COMMENT6_FOR_AD_2);

        int adId2 = commentRepository.findLastAdId();
        int countCommentByAd2 = commentRepository.countCommentsByAdId(adId2);

        //проверяем количество комментариев к объявлению до добавления нового
        mockMvc.perform(get("/ads/{id}/comments", adId2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(countCommentByAd2));

        //добавляем новый комментарий
        mockMvc.perform(post("/ads/{id}/comments", adId2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newComment.toString()))
                .andExpect(status().isOk());

        //проверяем количество комментариев к объявлению после добавления нового комментария
        mockMvc.perform(get("/ads/{id}/comments", adId2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(countCommentByAd2+1));

    }

    @Test
    void deleteComment() throws Exception {
        String base64Encoded = autorisationUser(USERNAME_USER1, PASSWORD_USER1);

        int adId2 = commentRepository.findLastAdId();
        int countCommentByAd2 = commentRepository.countCommentsByAdId(adId2);
        int lastCommentByAd2 = commentRepository.findLastCommentId(adId2);

        //проверяем количество комментариев к объявлению 2
        mockMvc.perform(get("/ads/{id}/comments", adId2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(countCommentByAd2));

        //пробуем удалить существующий комментарий из объявления 2
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", adId2, lastCommentByAd2-2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk());

        //проверяем, что количество комментариев уменьшилось
        mockMvc.perform(get("/ads/{id}/comments", adId2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(countCommentByAd2-1));
    }

    @Test
    void deleteComment_userWithoutAccess() throws Exception {
        String base64Encoded = autorisationUser(USERNAME_USER2, PASSWORD_USER2);

        int adId2 = commentRepository.findLastAdId();
        int countCommentByAd2 = commentRepository.countCommentsByAdId(adId2);
        int lastCommentByAd2 = commentRepository.findLastCommentId(adId2);

        //пробуем удалить чужой комментарий из объявления 2
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", adId2, lastCommentByAd2-2)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded))
                .andExpect(status().isForbidden());

    }

    @Test
    void updateComment() throws Exception {
        String base64Encoded = autorisationUser(USERNAME_USER1, PASSWORD_USER1);

        int adId1 = commentRepository.findLastAdId()-1;
        int lastCommentByAd1 = commentRepository.findLastCommentId(adId1);

        JSONObject comment = new JSONObject();
        comment.put("text", COMMENT6_FOR_AD_2);

        //пробуем изменить существующий комментарий из объявления 1
        mockMvc.perform(put("/ads/{adId}/comments/{commentId}", adId1, lastCommentByAd1)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comment.toString()))
                .andExpect(status().isOk());

        //пробуем изменить несуществующий комментарий из объявления 1
        mockMvc.perform(put("/ads/{adId}/comments/{commentId}", adId1, lastCommentByAd1+1)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comment.toString()))
                .andExpect(status().isNotFound());
    }


    @Test
    void updateComment_userWithoutAccess() throws Exception {
        String base64Encoded = autorisationUser(USERNAME_USER2, PASSWORD_USER2);

        int adId1 = commentRepository.findLastAdId()-1;
        int lastCommentByAd1 = commentRepository.findLastCommentId(adId1);

        JSONObject comment = new JSONObject();
        comment.put("text", COMMENT6_FOR_AD_2);

        //пробуем изменить чужой комментарий из объявления 1
        mockMvc.perform(put("/ads/{adId}/comments/{commentId}", adId1, lastCommentByAd1)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + base64Encoded)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(comment.toString()))
                .andExpect(status().isForbidden());

    }
}

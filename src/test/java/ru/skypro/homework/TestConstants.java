package ru.skypro.homework;

import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UsersRepository;

import java.time.LocalDateTime;

public class TestConstants {

    @Autowired
    private static CommentRepository commentRepository;

    @Autowired
    private static AdsRepository adsRepository;

    @Autowired
    private static UsersRepository usersRepository;

    public static final String USERNAME_ADMIN = "admin@mail.ru";
    public static final String USERNAME_USER1 = "user1@mail.ru";
    public static final String USERNAME_USER2 = "user2@mail.ru";
    public static final String PASSWORD_ADMIN = "admin1234";
    public static final String PASSWORD_ADMIN_ENCODED = "$2y$10$rrA0ENtX5XLspy/r3M2kYOSxEk6mrwRXuWDCp8egb4sy4lodKewjm";
    public static final String PASSWORD_USER1 = "user1234";
    public static final String PASSWORD_USER1_ENCODED = "$2y$10$zbfECZ2hT147ViBz3bSA6OUsXbDF43PVGev.4qLG4sY4irpi.4kh6";
    public static final String PASSWORD_USER2 = "user1234";
    public static final String PASSWORD_USER2_ENCODED = "$2y$10$nEux9hEDNxjBBf8ZMSDw6eOLpdQEo/H6NyFWRze7h1DVUNj/mpc7.";
    public static final String FIRST_NAME_ADMIN = "Admin First Name";
    public static final String FIRST_NAME_USER1 = "User1 First Name";
    public static final String FIRST_NAME_USER2 = "User2 First Name";
    public static final String LAST_NAME_ADMIN = "Admin Last Name";
    public static final String LAST_NAME_USER1 = "User1 Last Name";
    public static final String LAST_NAME_USER2 = "User2 Last Name";
    public static final String PHONE_ADMIN = "+7(999)999-99-99";
    public static final String PHONE_USER1 = "+7(111)111-11-11";
    public static final String PHONE_USER2 = "+7(222)222-22-22";
    public static final Role ROLE_ADMIN = Role.ADMIN;
    public static final Role ROLE_USER = Role.USER;
    public static final String ADMIN_KEY = "X-SECURITY-ADMIN-KEY";
    public static final String IMAGE_ADMIN = "user-icon.png";
    public static final String IMAGE_USER1 = "user-icon.png";
    public static final String IMAGE_USER2 = "user-icon.png";
    public static final String TITLE_AD1 = "advertisement1";
    public static final String TITLE_AD2 = "advertisement2";
    public static final String DESCRIPTION1="description advertisement1";
    public static final String DESCRIPTION2="description advertisement2";
    public static final int PRICE_AD1 = 1000;
    public static final int PRICE_AD2 = 2000;
    public static final String IMAGE_AD1="";
    public static final String IMAGE_AD2="";
    public static final LocalDateTime CREATED_AT_COMMENT1_FOR_AD1 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final LocalDateTime CREATED_AT_COMMENT2_FOR_AD1 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final LocalDateTime CREATED_AT_COMMENT3_FOR_AD1 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final LocalDateTime CREATED_AT_COMMENT4_FOR_AD2 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final LocalDateTime CREATED_AT_COMMENT5_FOR_AD2 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final LocalDateTime CREATED_AT_COMMENT6_FOR_AD2 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final LocalDateTime CREATED_AT_COMMENT7_FOR_AD2 = LocalDateTime.now().toLocalDate().atStartOfDay();
    public static final String COMMENT1_FOR_AD_1 = "Comment1 for Ad1";
    public static final String COMMENT2_FOR_AD_1 = "Comment2 for Ad1";
    public static final String COMMENT3_FOR_AD_1 = "Comment3 for Ad1";
    public static final String COMMENT4_FOR_AD_2 = "Comment4 for Ad2";
    public static final String COMMENT5_FOR_AD_2 = "Comment5 for Ad2";
    public static final String COMMENT6_FOR_AD_2 = "Comment6 for Ad2";
    public static final String COMMENT7_FOR_AD_2 = "Comment7 for Ad2";

}

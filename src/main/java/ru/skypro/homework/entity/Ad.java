package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ads")
public class Ad {


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Integer id;
//    @Column(name = "title")
//    @NotNull
//    private String title;
//    @OneToOne
//    @JoinColumn(name = "image_id")
//    private PosterEntity image;
//    @Column(name = "price")
//    @PositiveOrZero
//    @NotNull
//    private Integer price;
//    @Column(name = "description")
//    @NotNull
//    private String description;
//
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "author_id")
//    @NotNull
//    private UserEntity author;
//    @OneToMany(mappedBy = "ads")
//    @ToString.Exclude
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    private List<CommentEntity> results;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id",nullable = false)
    private Integer pk; //id объявления

    @Column(name = "title", nullable = false)
    private String title; //заголовок объявления

    @Column(name="description")
    private String description; // описание объявления

    @Column(name = "price", nullable = false)
    private int price; //цена объявления

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image; //  путь к картинке объявления

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users author; //id автора объявления

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> results; //комментарии к объявлению






}

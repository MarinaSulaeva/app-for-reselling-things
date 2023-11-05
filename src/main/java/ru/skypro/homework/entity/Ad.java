package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

/**
 * Класс-сущность с данными объявления в базе данных
 * @author Sayfullina Anna
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id",nullable = false)
    private Integer pk;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_ad_id")
    private ImageAd image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;






}

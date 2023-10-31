package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image_ad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAd {
    @Id
    private String id;

    private byte [] image;
}

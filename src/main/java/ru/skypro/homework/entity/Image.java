package ru.skypro.homework.entity;


import lombok.*;
import ru.skypro.homework.dto.Role;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id",nullable = false)
    private Integer id;

    @Column(name = "path")
    @NotNull
    private String path;

    public Image (String path) {
        this.path = path;
    }

}

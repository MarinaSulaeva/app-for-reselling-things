package ru.skypro.homework.repository;

import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Image;

import java.util.Optional;

@Repository
public interface ImageRepository {

    Optional<Image> findById(int id);
}

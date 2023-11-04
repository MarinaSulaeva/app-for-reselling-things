package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.entity.Ad;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdsRepository extends CrudRepository<Ad, Integer> {


    @Query(value = "SELECT * FROM ads", nativeQuery = true)
    List<Ad> findAllAds();


    @Query(value = "SELECT * FROM ads " +
            "WHERE user_id = :meId", nativeQuery = true)
    List<Ad> getAdsMe(Integer meId);

    Optional<Ad> findAdByPk(int pk);

    Optional<Ad> findAdByTitle(String title);



}

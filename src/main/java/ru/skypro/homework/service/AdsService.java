package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.*;

/**
 * Интерфейс с методами получения, добавления, изменения, удаления объявлений
 * @author Sayfullina Anna
 */
public interface AdsService {
    Ads getAllAds();

    AdDTO addAd(CreateOrUpdateAd createAd, MultipartFile image, Authentication authentication);

    ExtendedAd getAds(int id, Authentication authentication);

    ResponseEntity<Void> removeAd (int id, Authentication authentication);

    AdDTO updateAds(int id, CreateOrUpdateAd updateAd, Authentication authentication);

    Ads getAdsMe(Authentication authentication);

    ImageAdDTO updateImage(int id, MultipartFile image, Authentication authentication);

    byte [] getImage (String id);

}
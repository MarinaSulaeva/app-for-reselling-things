package ru.skypro.homework.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;

import java.io.IOException;


public interface AdsService {
    Ads getAllAds();
    AdDTO addAd(CreateOrUpdateAd properties, MultipartFile image, String username) throws IOException;

    ExtendedAd getAds(int id, Authentication authentication);

//    @PreAuthorize("principal.admin or #username == authentication.principal.username")
    void removeAd (int id, Authentication authentication);

//    @PreAuthorize("principal.admin or #username == authentication.principal.username")
    AdDTO updateAds(int id, CreateOrUpdateAd updateAd, Authentication authentication);

    Ads getAdsMe(Authentication authentication);

//    @PreAuthorize("principal.admin or #username == authentication.principal.username")
    String updateImage(int id,MultipartFile image, String username);

}
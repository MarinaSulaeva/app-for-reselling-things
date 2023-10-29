package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDTO;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.dto.ads.CreateOrUpdateAd;
import ru.skypro.homework.dto.ads.ExtendedAd;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exceptions.AccessErrorException;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AdsService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UsersRepository usersRepository;


    public AdsServiceImpl(AdsRepository adsRepository, UsersRepository usersRepository) {
        this.adsRepository = adsRepository;
        this.usersRepository = usersRepository;
    }

    private boolean isAdminOrOwnerAd(Authentication authentication, String ownerAd) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .contains("ROLE_ADMIN");

        boolean isOwnerAd = authentication.getName().equals(ownerAd);

        return isAdmin || isOwnerAd;

    }

    private Users userMe(Authentication authentication) {
        return usersRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Ads getAllAds() {

        List<Ad> adList = adsRepository.findAllAds();

        List<AdDTO> adDTOList = adList.stream()
                .map(AdDTO::fromAd)
                .collect(Collectors.toList());

        return new Ads(adDTOList);
    }

    @Override
    public AdDTO addAd(CreateOrUpdateAd properties, MultipartFile image, String username) throws IOException {
        Users user = usersRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        Ad ad = properties.toAd(user);
        ad.setAuthor(user);
        adsRepository.save(ad);
        List<String> SUPPORTED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
        String filename = image.getOriginalFilename();
        String type = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        //проверка, что переданный файл - изображение
//        if (!SUPPORTED_EXTENSIONS.contains(type)) {
//            throw new UnsupportedFormatException();
//        }
        byte[] newImage = image.getBytes();

        String pathString = "C:\\Users\\anna\\Pictures\\ads_image"  + image.getOriginalFilename().toLowerCase().replaceAll(" ", "-") + "." + type;
        Path uploadDir = Paths.get("C:\\Users\\anna\\Pictures\\ads_image");
        //проверка, что директория существует, если нет - создает ее
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        File fileImage = new File(pathString);
        Files.write(Paths.get(pathString), newImage);
        ad.setImage(fileImage.getPath());
        Ad newAd = adsRepository.save(ad);

//        File uploadDir = new File(appProperties.getUploadPath());
//        // Если директория uploads не существует, то создаем ее
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }


//        String userImageHome = System.getProperty("user_image.home");
//        File directory = new File(userImageHome, "new_image");
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        String curDate = LocalDateTime.now().toString();
//        // Создаем уникальное название для файла и загружаем файл
//        String fileName =
//                "image_" + curDate + "_" + image.getOriginalFilename().toLowerCase().replaceAll(" ", "-");
//        String filePath = directory + "/" + fileName;

//        String uploadDir = "C:/Users/anna/Pictures/ads_image";
//        File directory = new File(uploadDir);
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String curDate = LocalDateTime.now().toString();
//        String fileName = "image_" + curDate + "_" + image.getOriginalFilename().toLowerCase().replaceAll(" ", "-");
//        String filePath = directory + "/" + fileName + "." + "PNG";
//        File newImage = new File(filePath);
//
//        try {
////            image.transferTo(new File(filePath));
//            Files.write(Paths.get(filePath), image.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//
//        // Сохраняем файл на файловой системе
//        FileOutputStream fos = new FileOutputStream(filePath);
//        fos.write(file.getBytes());
//        fos.close();

//        ad.setImage(newImage.getPath());
//        Ad newAd = adsRepository.save(ad);
//        adsRepository.save(ad);

        return AdDTO.fromAd(newAd);
    }

    @Override
    public ExtendedAd getAds(int id, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            Ad extendedAd = adsRepository.findAdByPk(id).orElseThrow(AdNotFoundException::new);
            return ExtendedAd.fromAd(extendedAd);
        } else {
            throw new AccessErrorException();
        }
    }

    @Override
    public void removeAd(int id, Authentication authentication) {
        Ad deletedAd = adsRepository.findAdByPk(id).orElseThrow(AdNotFoundException::new);
        if (isAdminOrOwnerAd(authentication, deletedAd.getAuthor().getUsername())) {
            adsRepository.delete(deletedAd);
        } else {
            throw new AccessErrorException();
        }
    }

    @Override
    public AdDTO updateAds(int id, CreateOrUpdateAd updateAd, Authentication authentication) {
        Ad updatedAd = adsRepository.findAdByPk(id).orElseThrow(AdNotFoundException::new);
        if (isAdminOrOwnerAd(authentication, updatedAd.getAuthor().getUsername())) {
            updatedAd.setTitle(updateAd.getTitle());
            updatedAd.setPrice(updateAd.getPrice());
            updatedAd.setDescription(updateAd.getDescription());
            adsRepository.save(updatedAd);
        } else {
            throw new AccessErrorException();
        }

        return AdDTO.fromAd(updatedAd);
    }


    @Override
    public Ads getAdsMe(Authentication authentication) {
        Integer meId = userMe(authentication).getId();
        return new Ads(adsRepository.getAdsMe(meId)
                .stream()
                .map(AdDTO::fromAd)
                .collect(Collectors.toList()));
    }

    @Override
    public String updateImage(int id, MultipartFile image, String username) {
        return null;
    }
}
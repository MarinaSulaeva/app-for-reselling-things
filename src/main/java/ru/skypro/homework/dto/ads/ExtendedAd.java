package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Users;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedAd {

    private int pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private int price;
    private String title;

    public static ExtendedAd fromAd(Ad ad){
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(ad.getPk());
        extendedAd.setAuthorFirstName(ad.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(ad.getAuthor().getLastName());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setEmail(ad.getAuthor().getUsername());
        extendedAd.setPhone(ad.getAuthor().getPhone());
        extendedAd.setTitle(ad.getTitle());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setImage(ad.getImage());
        return extendedAd;
    }


}
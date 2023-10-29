package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Users;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO {
    private int pk;
    private int author;
    private String title;
    private int price;
    private String image;


    public static AdDTO fromAd(Ad ad){
        AdDTO adDTO = new AdDTO();
        adDTO.setPk(ad.getPk());
        adDTO.setAuthor(ad.getAuthor().getId());
        adDTO.setTitle(ad.getTitle());
        adDTO.setPrice(ad.getPrice());
        adDTO.setImage(ad.getImage().getPath());
        return adDTO;
    }

//    public Ad toAd(){
//        Ad ad = new Ad();
//        ad.setPk(this.getPk());
//        ad.setAuthor(this.);
//        ad.setTitle(this.getTitle());
//        ad.setPrice(this.getPrice());
//        ad.setImage(this.getImage());
//        return ad;
//    }
}
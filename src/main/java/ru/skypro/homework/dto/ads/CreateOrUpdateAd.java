package ru.skypro.homework.dto.ads;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.Ad;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * Класс-обертка для добавления и/или обновления объявления с валидацией
 * @author Sayfullina Anna
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateAd {

    @Size(min = 4, max = 32)
    private String title;

    @Max(10000000)
    private int price;

    @Size(min = 8, max = 64)
    private String description;

    public Ad toAd(){
        Ad ad = new Ad();
        ad.setTitle(this.getTitle());
        ad.setPrice(this.getPrice());
        ad.setDescription(this.getDescription());
        return ad;
    }

}
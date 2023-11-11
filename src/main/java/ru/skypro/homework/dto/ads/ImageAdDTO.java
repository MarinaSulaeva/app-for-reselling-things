package ru.skypro.homework.dto.ads;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.ImageAd;

/**
 * Класс-обертка для получения картинки объявления
 *
 * @author Sayfullina Anna
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAdDTO {

    private String id;
    private byte[] image;

    public static ImageAdDTO fromImageAd(ImageAd image) {
        return new ImageAdDTO(image.getId(), image.getImage());
    }

}

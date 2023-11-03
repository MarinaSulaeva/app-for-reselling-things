package ru.skypro.homework.dto.ads;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.ImageAd;
/**
 * The class-wrapper for getting image
 * @author Sayfullina Anna
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAdDTO {

    private String id;

    private byte [] image;

    /**
     * The method for mapping from entity ImageAd to class-wrapper
     */
    public static ImageAdDTO fromImageAd (ImageAd image) {
        return new ImageAdDTO(image.getId(), image.getImage());
    }

}

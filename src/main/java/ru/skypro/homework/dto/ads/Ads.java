package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс-обертка для получения списка объявлений и их количества
 * @author Sayfullina Anna
 */
@Data
@NoArgsConstructor
public class Ads {

    private int count;
    private List<AdDTO> results;

    public Ads(List<AdDTO> results) {
        this.results = results;
        this.count = results.size();
    }
}
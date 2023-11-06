package ru.skypro.homework.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Класс-обертка для получения списка комментариев к объявлению и их количества
 *
 * @author Морозова Светлана
 */

@Data
@AllArgsConstructor
public class Comments {

    private Integer count;
    private List<CommentDTO> results;

}

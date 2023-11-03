package ru.skypro.homework.dto.comment;


import lombok.Data;

import javax.validation.constraints.Size;

/**
 * Класс-обертка для добавления и/или обновления комментария к объявлению с валидацией размера комментария
 * @author Морозова Светлана
 */

@Data
public class CreateOrUpdateComment {

    @Size(message = "введите текст комментария от 8 до 64 символов", min = 8, max = 64)
    private String text;

}

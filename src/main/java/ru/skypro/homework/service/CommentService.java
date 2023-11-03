package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

/**
 * Интерфейс с методами получения, добавления, изменения, удаления комментариев к объявлениям
 * @author Морозова Светлана
 */

public interface CommentService {

    Comments getComments(Integer adId, Authentication authentication);

    CommentDTO addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication);

    void deleteComment(Integer adId, Integer commentId, Authentication authentication);

    CommentDTO updateComment(Integer adId, Integer commentId,CreateOrUpdateComment createOrUpdateComment, Authentication authentication);
}

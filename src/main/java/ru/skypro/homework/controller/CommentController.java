package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;

/**
 * Класс-контроллер для запуска эндпоинтов, относящихся к комментариям объявлений
 *
 * @author Морозова Светлана
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("ads")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    /**
     * Метод для получения всех комментариев к объявлению
     */

    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer adId,
                                                Authentication authentication) {
        logger.info("get all comments");
        return ResponseEntity.ok(commentService.getComments(adId, authentication));
    }

    /**
     * Метод для Добавление комментария к объявлению
     */

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable("id") Integer adId,
                                                 @RequestBody @Valid CreateOrUpdateComment createOrUpdateComment,
                                                 Authentication authentication) {
        logger.info("add new comment");
        return ResponseEntity.ok(commentService.addComment(adId, createOrUpdateComment, authentication));
    }

    /**
     * Метод для Удаления комментария у объявления
     */

    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable("adId") Integer adId,
                              @PathVariable("commentId") Integer commentId,
                              Authentication authentication) {
        logger.info("delete comment");
        commentService.deleteComment(adId, commentId, authentication);
    }

    /**
     * Метод для Обновления комментария у объявления
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentDTO updateComment(@PathVariable("adId") Integer adId,
                                    @PathVariable("commentId") Integer commentId,
                                    @RequestBody @Valid CreateOrUpdateComment createOrUpdateComment,
                                    Authentication authentication) {
        logger.info("update comment");

        return commentService.updateComment(adId, commentId, createOrUpdateComment, authentication);
    }
}

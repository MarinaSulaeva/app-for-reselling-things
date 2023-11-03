package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.exceptions.AccessErrorException;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, содержащий методы получения, добавления, изменения, удаления комментариев к объявлениям
 *
 * @author Морозова Светлана
 */

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UsersRepository usersRepository;

    public CommentServiceImpl(CommentRepository commentRepository, AdsRepository adsRepository, UsersRepository usersRepository) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * Внутренний метод класса, проверяющий право пользователя на изменение или удаление комментария
     */
    private boolean isAdminOrOwnerComment(Authentication authentication, String ownerComment) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .contains("ROLE_ADMIN");

        boolean isOwnerComment = authentication.getName().equals(ownerComment);

        return isAdmin || isOwnerComment;

    }

    /**
     * Публичный метод получения всех комментариев объявления и их количества
     */
    @Override
    public Comments getComments(Integer adId, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            List<CommentDTO> commentDTOList = commentRepository.findAllCommentsByAdId(adId)
                    .stream()
                    .map(CommentDTO::fromComment)
                    .collect(Collectors.toList());
            return new Comments(commentDTOList.size(), commentDTOList);
        } else {
            throw new AccessErrorException();
        }

    }

    /**
     * Публичный метод добавления комментария к объявлению
     */
    @Override
    public CommentDTO addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {

        if (authentication.isAuthenticated()) {
            String username = authentication.getName();

            Ad getAd = adsRepository.findAdByPk(id).orElseThrow(AdNotFoundException::new);
            Users meUsers = usersRepository.findByUsername(username)
                    .orElseThrow(UserNotFoundException::new);
            Comment newComment = new Comment();

            newComment.setUsers(meUsers);
            newComment.setAd(getAd);
            newComment.setText(createOrUpdateComment.getText());
            newComment.setCreatedAt(LocalDateTime.now());

            CommentDTO commentDTO = CommentDTO.fromComment(commentRepository.save(newComment));

            return commentDTO;
        } else {
            throw new AccessErrorException();
        }
    }

    /**
     * Публичный метод удаления комментария к объявлению, доступен только автору комментария и администратору
     */
    @Override
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            if (!adId.equals(findComment.getAd().getPk())) {
                throw new CommentNotFoundException();
            } else {
                if (isAdminOrOwnerComment(authentication, findComment.getUsers().getUsername())) {
                    commentRepository.delete(findComment);
                } else {
                    throw new AccessErrorException();
                }
            }
        } else {
            throw new AccessErrorException();
        }
    }

    /**
     * Публичный метод изменения комментария к объявлению, доступен только автору комментария и администратору
     */
    @Override
    public CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            if (!adId.equals(findComment.getAd().getPk())) {
                throw new CommentNotFoundException();
            } else {

                if (isAdminOrOwnerComment(authentication, findComment.getUsers().getUsername())) {
                    findComment.setText(createOrUpdateComment.getText());
                    findComment.setCreatedAt(LocalDateTime.now());

                    CommentDTO commentDTO = CommentDTO.fromComment(commentRepository.save(findComment));
                    return commentDTO;
                } else {
                    throw new AccessErrorException();
                }

            }
        } else {
            throw new AccessErrorException();
        }
    }
}

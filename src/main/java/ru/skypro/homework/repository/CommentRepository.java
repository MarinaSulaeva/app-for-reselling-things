package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    @Query(value = "SELECT * FROM comments " +
            "WHERE ad_id = :AdId",
            nativeQuery = true)
    List<Comment> findAllCommentsByAdId(Integer AdId);

    @Query(value = "SELECT COUNT(comment_id) FROM comments " +
            "WHERE ad_id = :AdId",
            nativeQuery = true)
    Integer countCommentsByAdId(Integer AdId);
    @Query(value = "SELECT MAX(comment_id) FROM comments " +
            "WHERE ad_id = :AdId",
            nativeQuery = true)
    Integer findLastCommentId(Integer AdId);

    @Query(value = "SELECT COUNT(ad_id) FROM ads",
            nativeQuery = true)
    Integer countAdId();

    @Query(value = "SELECT MAX(ad_id) FROM ads",
            nativeQuery = true)
    Integer findLastAdId();


}

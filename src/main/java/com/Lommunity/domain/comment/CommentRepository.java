package com.Lommunity.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c from comments c join fetch c.user u join fetch u.region where c.id = :commentId")
    Optional<Comment> findWithUserByCommentId(@Param("commentId") Long commentId);
    Page<Comment> findCommentPageByPostId(Long postId, Pageable pageable);
}

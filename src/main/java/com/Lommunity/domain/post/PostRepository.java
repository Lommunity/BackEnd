package com.Lommunity.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p from posts p join fetch p.user u join fetch u.region where p.id = :postId")
    Optional<Post> findWithUserByPostId(@Param("postId") Long postId);

    Page<Post> findPostAllPageBySecondRegionLevel(Long secondRegionLevel, Pageable pageable);

    Page<Post> findPostPageByUserId(Long userId, Pageable pageable);

    Page<Post> findPostPageBySecondRegionLevelAndTopicId(Long secondRegionLevel, Long topicId, Pageable pageable);

    @Query(value = "select p from posts p where p.secondRegionLevel = :secondRegionLevel and p.content like %:word%")
    Page<Post> findPostBySecondRegionLevelAndWord(@Param("secondRegionLevel") Long secondRegionLevel, @Param("word") String word, Pageable pageable);
}

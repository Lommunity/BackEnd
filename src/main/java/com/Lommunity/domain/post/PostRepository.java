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

    Page<Post> findPostAllPageBySecondLevelRegionCode(Long secondRegionLevel, Pageable pageable);

    Page<Post> findPostPageByUserId(Long userId, Pageable pageable);

    Page<Post> findPostPageBySecondLevelRegionCodeAndTopicId(Long secondLevelRegionCode, Long topicId, Pageable pageable);

    @Query(value = "select p from posts p where p.secondLevelRegionCode = :secondLevelRegionCode and p.content like %:word%")
    Page<Post> findPostBySecondLevelRegionCodeAndWord(@Param("secondLevelRegionCode") Long secondLevelRegionCode, @Param("word") String word, Pageable pageable);
}

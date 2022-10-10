package com.Lommunity.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByTargetTypeAndTargetIdAndUserId(LikeTarget targetType, Long targetId, Long userId);
    Optional<Like> findByTargetTypeAndTargetIdAndUserId(LikeTarget targetType, Long targetId, Long userId);
    Long countByTargetTypeAndTargetId(LikeTarget targetType, Long targetId);
}

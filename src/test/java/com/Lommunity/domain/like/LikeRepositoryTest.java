package com.Lommunity.domain.like;

import com.Lommunity.domain.post.Post;
import com.Lommunity.domain.user.User;
import com.Lommunity.testhelper.EntityTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LikeRepositoryTest {

    @Autowired
    EntityTestHelper entityTestHelper;
    @Autowired
    LikeRepository likeRepository;

    @Test
    public void likeCountByPostIdTest() {
        // given
        User user1 = entityTestHelper.userRegisterBusan("홍길동");
        User user2 = entityTestHelper.userRegisterBusan("돼지");
        User user3 = entityTestHelper.userRegisterBusan("감자");

        Post post = entityTestHelper.createPost(user1);
        Like like1 = entityTestHelper.createLike(user1, LikeTarget.POST, post.getId());
        Like like2 = entityTestHelper.createLike(user2, LikeTarget.POST, post.getId());
        Like like3 = entityTestHelper.createLike(user3, LikeTarget.POST, post.getId());

        // when
        Long likeCountByPostId = likeRepository.countByTargetTypeAndTargetId(LikeTarget.POST, post.getId());

        // then
        assertThat(likeCountByPostId).isEqualTo(3L);
    }

    @Test
    public void existsLikeByPostIdAndUserId() {
        // given
        User post1Writer = entityTestHelper.userRegisterBusan("홍길동");
        User post2Writer = entityTestHelper.userRegisterBusan("돼지");

        Post post1 = entityTestHelper.createPost(post1Writer);
        Post post2 = entityTestHelper.createPost(post2Writer);

        // when
        entityTestHelper.createLike(post1Writer, LikeTarget.POST, post1.getId()); // 게시글 작성자 id == 좋아요 한 사람 id
        entityTestHelper.createLike(post2Writer, LikeTarget.POST, post1.getId()); // 게시글 작성자 id != 좋아요 한 사람 id

        // then

        // post1을 user1(게시글 작성자)가 좋아요를 눌렀는가 ? YES
        // post2를 user2(게시글 작성자)가 좋아요를 눌렀는가 ? NO
        assertThat(likeRepository.existsByTargetTypeAndTargetIdAndUserId(LikeTarget.POST, post1.getId(), post1Writer.getId())).isEqualTo(true);
        assertThat(likeRepository.existsByTargetTypeAndTargetIdAndUserId(LikeTarget.POST, post2.getId(), post2Writer.getId())).isEqualTo(false);
    }
}
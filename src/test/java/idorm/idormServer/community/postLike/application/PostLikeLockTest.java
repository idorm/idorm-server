package idorm.idormServer.community.postLike.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.community.post.application.port.out.LoadPostPort;
import idorm.idormServer.community.post.entity.Post;

@SpringBootTest
public class PostLikeLockTest {

	@Autowired
	private PostLikeService postLikeService;
	@Autowired
	private LoadPostPort loadPostPort;

	private List<AuthResponse> authResponses = List.of(
		new AuthResponse(2L, "MEMBER", "나도미"),
		new AuthResponse(3L, "MEMBER", "나도미"),
		new AuthResponse(4L, "MEMBER", "나도미"),
		new AuthResponse(5L, "MEMBER", "나도미"),
		new AuthResponse(6L, "MEMBER", "나도미"),
		new AuthResponse(7L, "MEMBER", "나도미"),
		new AuthResponse(8L, "MEMBER", "나도미"),
		new AuthResponse(9L, "MEMBER", "나도미"),
		new AuthResponse(10L, "MEMBER", "나도미"),
		new AuthResponse(11L, "MEMBER", "나도미"));

	@Test
	void 게시글_하나에_10명이_동시에_좋아요_요청() throws InterruptedException {
		//given
		final int PARTICIPATION_PEOPLE = 10;
		final Long postId = 1L;
		CountDownLatch countDownLatch = new CountDownLatch(PARTICIPATION_PEOPLE);
		ExecutorService executorService = Executors.newFixedThreadPool(PARTICIPATION_PEOPLE);
		AtomicInteger successCount = new AtomicInteger();

		//when
		for (AuthResponse authResponse : authResponses) {
			executorService.execute(() -> {
				try {
					postLikeService.save(authResponse, postId);
					successCount.getAndIncrement();
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		countDownLatch.await();
		executorService.shutdown();

		//then
		Post post = loadPostPort.findById(1L);
		assertThat(post.getLikeCount()).isEqualTo(PARTICIPATION_PEOPLE);
	}
}

package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 기숙사 카테고리로 필터링한 게시글들 조회
     */
    Page<Post> findAllByDormNumAndIsDeletedOrderByCreatedAtDesc(String dormNum, Boolean isDeleted, Pageable pageable);


    /**
     * 인기 게시글 찾는 로직
     * 기숙사 분류를 통한 일차 필터링
     * 생성일자 기준으로 7일 이내의 글 중에서만 조회
     * 공감 순으로 상위 10개 조회 (만약 동일 공감이 많다면 더 빠른 최신 날짜로)
     */
    // TODO: SQL syntax err 발생
    @Query(value = "SELECT *" +
            "FROM post p " +
            "WHERE p.dorm_num = :dormNum AND " +
            "p.created_at BETWEEN DATE_ADD(NOW(), INTERVAL -1 WEEK) AND NOW() AND " +
            "p.is_deleted = 0 " +
            "ORDER BY p.likes_count DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Post> findTopPostsByDormCategory(@Param("dormNum") String dormNum);

    /**
     * 멤버 아이디를 통해서 멤버가 작성한 게시글 반환
     */
    List<Post> findAllByMemberIdAndIsDeletedOrderByUpdatedAtDesc(Long memberId, Boolean isDeleted);
}

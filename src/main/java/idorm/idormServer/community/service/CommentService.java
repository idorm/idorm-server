package idorm.idormServer.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    /**
     * Comment 저장(생성) |
     */


    /**
     * Comment 단건 조회 (해당 댓글의 전체 대댓글까지 조회되게)
     */

    /**
     * 로그인한 멤버가 작성한 모든 댓글들 조회
     */

    /**
     * Comment 삭제 (여전히 대댓글은 살아있어야함)
     */
}

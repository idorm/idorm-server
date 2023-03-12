package idorm.idormServer.development.service;

import idorm.idormServer.calendar.repository.CalendarRepository;
import idorm.idormServer.community.repository.CommentRepository;
import idorm.idormServer.community.repository.PostLikedMemberRepository;
import idorm.idormServer.community.repository.PostRepository;
import idorm.idormServer.development.inject.DataInjector;
import idorm.idormServer.email.repository.EmailRepository;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import idorm.idormServer.member.repository.MemberRepository;
import idorm.idormServer.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final DataInjector dataInjector;
    private final CalendarRepository calendarRepository;
    private final CommentRepository commentRepository;
    private final PostLikedMemberRepository postLikedMemberRepository;
    private final PostRepository postRepository;
    private final EmailRepository emailRepository;
    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;

    /**
     * 데이커베이스 초기화 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void resetDatabase() {

        try {
            calendarRepository.deleteAll();
            commentRepository.deleteAll();
            postLikedMemberRepository.deleteAll();
            postRepository.deleteAll();
            photoRepository.deleteAll();
            matchingInfoRepository.deleteAll();
            emailRepository.deleteAll();
            memberRepository.deleteAll();

            this.entityManager
                    .createNativeQuery("ALTER TABLE calendar AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE comment AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE post_liked_member AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE post AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE photo AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE matching_info AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE email AUTO_INCREMENT = 1")
                    .executeUpdate();

            this.entityManager
                    .createNativeQuery("ALTER TABLE member AUTO_INCREMENT = 1")
                    .executeUpdate();

            dataInjector.run(null);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}

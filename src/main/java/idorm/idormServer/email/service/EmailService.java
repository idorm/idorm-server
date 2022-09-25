package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.repository.EmailRepository;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

    private final EmailRepository emailRepository;

    /**
     * Email 저장 |
     * 이메일을 저장한다. 저장 중 디비에서 에러가 발생하면 500(Internal Server Error)을 던진다.
     */
    @Transactional
    public Long save(String email, String code){

        log.info("START | Email 저장 At " + LocalDateTime.now() + " | " + email);

        Email certifiedEmail = new Email(email, code);

        try {
            emailRepository.save(certifiedEmail);
        } catch (Exception e) {
            throw new InternalServerErrorException("Email save 중 서버 에러 발생", e);
        }

        log.info("COMPLETE | Email 저장 At " + LocalDateTime.now() + " | " + certifiedEmail.toString());
        return certifiedEmail.getId();
    }

    /**
     * Email 인증코드 저장 |
     * 이메일 인증코드를 저장한다. 이메일을 찾지 못하면 404(Not Found)를 던진다.
     */
    @Transactional
    public void setCode(String email, String code){

        log.info("START | Email 인증코드 저장 At " + LocalDateTime.now() + " | " + email);

        Optional<Email> nonCertifiedEmail = emailRepository.findByEmail(email);

        if(nonCertifiedEmail.isEmpty()) {
            throw new NotFoundException("인증코드를 저장할 email을 찾을 수 없습니다.");
        }

        nonCertifiedEmail.get().setCode(code);
        log.info("COMPLETE | Email 인증코드 저장 At " + LocalDateTime.now() + " | " + nonCertifiedEmail.get().getCode());
    }

    /**
     * Email(Optional) 조회 |
     * 이메일로 Email을 조회한다.
     */
    public Optional<Email> findByEmailOp(String email){

        log.info("COMPLETE | Email Optional 조회 At " + LocalDateTime.now() + " | " + email);
        return emailRepository.findByEmail(email);
    }

    /**
     * Email 조회 |
     * 이메일로 Email을 조회한다. 이메일을 찾지 못하면 404(Not Found)를 던진다.
     */
    public Email findByEmail(String email){

        log.info("START | Email 조회 At " + LocalDateTime.now() + " | " + email);

        Optional<Email> foundEmail = emailRepository.findByEmail(email);

        if(foundEmail.isEmpty()) {
            throw new UnauthorizedException("등록되지 않은 이메일입니다.");
        }

        log.info("COMPLETE | Email 조회 At " + LocalDateTime.now() + " | " + foundEmail.get().getEmail());
        return foundEmail.get();
    }

    /**
     * Email 삭제 |
     * 이메일 식별자를 통해 이메일을 삭제한다. 식별자로 이메일 조회에 실패하면 404(Not Found)를 던진다.
     * 삭제 중에 에러가 발생하면 500(Internal Server Error)를 던진다.
     */
    @Transactional
    public void deleteById(Long emailId){

        log.info("START | Email 삭제 At " + LocalDateTime.now() + " | " + emailId);

        Optional<Email> email = emailRepository.findById(emailId);

        if(email.isEmpty()) {
            throw new NotFoundException("이메일을 찾을 수 없습니다.");
        }

        try {
            emailRepository.delete(email.get());
        } catch (Exception e) {
            throw new InternalServerErrorException("Email 삭제 중 서버 에러 발생", e);
        }

        log.info("COMPLETE | Email 삭제 At " + LocalDateTime.now() + " | " + email.get().getEmail());
    }

    /**
     * Email 인증여부 체크 |
     * 이메일로 이메일 조회에 실패하면 404(Not Found)를 던진다.
     */
    @Transactional
    public void isChecked(String email){

        log.info("START | Email 인증여부 체크 At " + LocalDateTime.now() + " | " + email);

        Optional<Email> foundEmail = emailRepository.findByEmail(email);

        if(foundEmail.isEmpty()) {
            throw new NotFoundException("이메일을 찾을 수 없습니다.");
        }

        foundEmail.get().isChecked();
        log.info("COMPLETE | Email 인증여부 체크 At " + LocalDateTime.now() + " | " + foundEmail.get().getEmail());
    }

    /**
     * Email 가입여부 체크 |
     * 비밀번호 변경 시 가입여부를 체크해야할 때 사용한다.
     */
    @Transactional
    public void updateIsJoined(String email) {
        log.info("START | Email 가입여부 체크 At " + LocalDateTime.now() + " | " + email);
        Optional<Email> foundEmail = emailRepository.findByEmail(email);

        if(foundEmail.isEmpty()) {
            throw new NotFoundException("이메일을 찾을 수 없습니다.");
        }
        foundEmail.get().isJoined();
        emailRepository.save(foundEmail.get());
        log.info("COMPLETE | Email 가입여부 체크 At " + LocalDateTime.now() + " | " + email);
    }

}
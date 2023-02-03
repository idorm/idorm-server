package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.repository.EmailRepository;
import idorm.idormServer.exception.CustomException;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

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
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] EmailService save {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
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

        Email nonCertifiedEmail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        nonCertifiedEmail.setCode(code);
        log.info("COMPLETE | Email 인증코드 저장 At " + LocalDateTime.now() + " | " + email);
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

        Email foundEmail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        log.info("COMPLETE | Email 조회 At " + LocalDateTime.now() + " | " + email);
        return foundEmail;
    }

    /**
     * Email 삭제 |
     * 이메일 식별자를 통해 이메일을 삭제한다. 식별자로 이메일 조회에 실패하면 404(Not Found)를 던진다.
     * 삭제 중에 에러가 발생하면 500(Internal Server Error)를 던진다.
     */
    @Transactional
    public void deleteById(Long emailId){

        log.info("START | Email 삭제 At " + LocalDateTime.now() + " | 이메일 식별자 : " + emailId);

        Email email = emailRepository.findById(emailId)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        try {
            emailRepository.delete(email);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] EmailService deleteById {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }

        log.info("COMPLETE | Email 삭제 At " + LocalDateTime.now() + " | 이메일 : " + email.getEmail());
    }

    /**
     * Email 인증여부 체크 |
     * 이메일로 이메일 조회에 실패하면 404(Not Found)를 던진다.
     */
    @Transactional
    public void isChecked(String email){

        log.info("START | Email 인증여부 체크 At " + LocalDateTime.now() + " | " + email);

        Email foundEmail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        try {
            foundEmail.isChecked();
            emailRepository.save(foundEmail);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] EmailService isChecked {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        log.info("COMPLETE | Email 인증여부 체크 At " + LocalDateTime.now() + " | " + email);
    }

    /**
     * Email 가입여부 체크 |
     * 비밀번호 변경 시 가입여부를 체크해야할 때 사용한다.
     */
    @Transactional
    public void updateIsJoined(String email) {
        log.info("START | Email 가입여부 수정 At " + LocalDateTime.now() + " | " + email);

        Email foundEmail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        try {
            foundEmail.isJoined();
            emailRepository.save(foundEmail);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] EmailService updateIsJoined {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        log.info("COMPLETE | Email 가입여부 수정 At " + LocalDateTime.now() + " | " + email);
    }

    /**
     * Email updatedAt 수정 |
     * 이메일 전송 시 인증기간 만료 체크를 위해 updatedAt을 현재 시간으로 수정해야 한다.
     */
    @Transactional
    public void updateUpdatedAt(Email email) {
        log.info("START | Email updatedAt 수정 At " + LocalDateTime.now() + " | " + email);

        try {
            email.modifyUpdatedAt(LocalDateTime.now());
            emailRepository.save(email);
        } catch (DataAccessException | ConstraintViolationException e) {
            log.info("[서버 에러 발생] EmailService updateUpdatedAt {} {}", e.getCause(), e.getMessage());
            throw new CustomException(SERVER_ERROR);
        }
        log.info("COMPLETE | Email updatedAt 수정 At " + LocalDateTime.now() + " | " + email);
    }

}
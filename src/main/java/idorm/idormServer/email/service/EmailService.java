package idorm.idormServer.email.service;

import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.repository.EmailRepository;
import idorm.idormServer.exception.CustomException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

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

        Email certifiedEmail = new Email(email, code);

        try {
            emailRepository.save(certifiedEmail);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
        return certifiedEmail.getId();
    }

    /**
     * Email 인증코드 저장 |
     * 이메일 인증코드를 저장한다. 이메일을 찾지 못하면 404(Not Found)를 던진다.
     */
    @Transactional
    public void setCode(String email, String code){

        Email nonCertifiedEmail = emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));

        nonCertifiedEmail.setCode(code);
    }

    public boolean isExistingEmail(String email) {
        return emailRepository.existsByEmail(email);
    }

    /**
     * Email 조회 |
     * 404(EMAIL_NOT_FOUND)
     */
    public Email findByEmail(String email){

        return emailRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, EMAIL_NOT_FOUND));
    }

    /**
     * Email 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteEmail(Email email){

        try {
            emailRepository.delete(email);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * Email 인증여부 true 체크 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateIsChecked(Email email){

        try {
            email.isChecked();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * Email 인증여부 false 체크 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateIsUnChecked(Email email) {
        try {
            email.isUnChecked();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * Email 가입여부 체크 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateIsJoined(Email email) {
        try {
            email.isJoined();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * Email updatedAt 수정 |
     * 이메일 전송 시 인증기간 만료 체크를 위해 updatedAt을 현재 시간으로 수정해야 한다. |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateUpdatedAt(Email email) {
        try {
            email.modifyUpdatedAt(LocalDateTime.now());
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

}
package idorm.idormServer.service;

import idorm.idormServer.domain.Email;
import idorm.idormServer.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class EmailService {

    private final JavaMailSender emailSender;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmailRepository emailRepository;

    @Transactional
    public Long save(String email,String code){
        Email email1=new Email(email, code);
        emailRepository.save(email1);
        return email1.getId();
    }
    @Transactional
    public void setCode(String email,String code){

        Email email1=emailRepository.findByEmail(email).get();
        email1.setCode(code);

    }
    public Optional<Email> findByEmailOp(String email){
        return emailRepository.findByEmail(email);
    }
    public Email findByEmail(String email){
        return emailRepository.findByEmail(email).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일이 존재하지 않습니다."));

    }
    @Transactional
    public void deleteById(Long id){

        Email email = emailRepository.findById(id).get();
        email.isLeft();
    }
    @Transactional
    public void isChecked(String email){
        emailRepository.findByEmail(email).get().isChecked();
    }



}
package idorm.idormServer.auth.encryptor;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Encryptor implements EncryptorI {
    @Override
    public String encrypt(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(text.getBytes());
            return bytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            //"SHA-256" 외부 라이브러리가 없습니다.
            throw new CustomException(null, ExceptionCode.SERVER_ERROR);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}

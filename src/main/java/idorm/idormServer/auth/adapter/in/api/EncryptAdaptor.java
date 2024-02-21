package idorm.idormServer.auth.adapter.in.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptAdaptor implements EncryptPort {

	@Override
	public String encrypt(String text) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(text.getBytes());
			return bytesToHex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			//"SHA-256" 외부 라이브러리가 없습니다.
			e.printStackTrace();
		}
		// TODO: 수정 예정
		return null;
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
}

package idorm.idormServer.auth.adapter.in.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EncryptAdaptor implements EncryptPort {

	@Override
	public String encrypt(String text) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(text.getBytes());
		} catch (NoSuchAlgorithmException e) {
			//"SHA-256" 외부 라이브러리가 없습니다.
			e.printStackTrace();
		}
		return bytesToHex(messageDigest.digest());
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
}

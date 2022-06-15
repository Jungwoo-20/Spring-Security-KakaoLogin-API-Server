package com.ssafy.happyhouse.security;

import java.math.BigInteger;
import java.security.*;
import java.util.*;

//Java Password Auth Reference
//https://www.javaguides.net/2020/02/java-sha-256-hash-with-salt-example.html
public class PasswordAuth {

	private SecureRandom secureRandom;
	private MessageDigest messageDigest;

	// 싱글턴 홀더 적용
	private PasswordAuth() {
	}

	private static class InnerPasswordAuth {
		private static final PasswordAuth PASSWORD_AUTH = new PasswordAuth();
	}

	public static PasswordAuth getInstance() {
		return InnerPasswordAuth.PASSWORD_AUTH;
	}
	// 싱글턴 구간 종료

	// 회원가입 salt 발급
	public String saltValueIssued() throws NoSuchAlgorithmException {
		// SHA1PRNG Reference
		// https://www.ibm.com/docs/en/sdk-java-technology/7.1?topic=guide-securerandom-provider
		secureRandom = SecureRandom.getInstance("SHA1PRNG");
		byte[] randomBytes = new byte[16];
		secureRandom.nextBytes(randomBytes);
		String saltValue = new String(Base64.getEncoder().encode(randomBytes));
		return saltValue;
	}

	// 암호화 일치 여부 판단
	public String passwordAuth(String password, String saltValue) throws NoSuchAlgorithmException {
		messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes());
		String hashPasswordValue = String.format("%064x",
				new BigInteger(1, messageDigest.digest(saltValue.getBytes())));
		return hashPasswordValue;
	}

}

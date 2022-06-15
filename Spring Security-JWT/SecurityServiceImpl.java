package com.ssafy.happyhouse.security;

import java.awt.RenderingHints.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SecurityServiceImpl implements SecurityService {
	private static final String secretKey = "암호 비밀키 입력";

	@Override
	public String createToken(String subject, long ttlMillins) {
		// TODO Auto-generated method stub
		// 토근 유효기간 만료
		if (ttlMillins == 0) {
			return "토큰 유효기간이 만료되었습니다.";
		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeyBytes = DatatypeConverter.parseBase64Binary(secretKey);
		SecretKeySpec signingKey = new SecretKeySpec(apiKeyBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setSubject(subject).signWith(signatureAlgorithm, signingKey);

		long nowMillis = System.currentTimeMillis();
		builder.setExpiration(new Date(nowMillis + ttlMillins));
		return builder.compact();
	}

	@Override
	public String getSubject(String token) {
		// TODO Auto-generated method stub
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

}

package com.ssafy.happyhouse.security;

public interface SecurityService {
	// 토근 발급
	public String createToken(String subject, long ttlMillins);
	
	// 유효성 검사
	public String getSubject(String token);
}

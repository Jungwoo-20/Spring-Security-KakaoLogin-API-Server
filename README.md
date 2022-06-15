# SpringBoot-KakaoLogin-API

## 스프링 부트(SpringBoot) + 카카오 로그인(Kakao Login) OAuth

### 요약
1. Client에서 카카오 로그인 버튼을 통한 로그인 수행
2. 로그인 성공 시 Code값에 기반해 회원 정보 등록 수행을 위해 Spring 서버로 Code 전송
3. 요청 URL의 Code값 추출
4. Code를 통해 카카오 Token 값 발급
5. Token 값을 통해 사용자 정보 조회
6. 자체 DBMS 등록 여부 판단 후 회원가입 로직 수행 및 로그인 완료 처리 반환

### 2022-06-15 추가
#### JWT를 활용한 로그인 예제
```
1. PasswordAuth.getInstance()를 통한 싱글턴 객체 발급
2. saltValueIssued()를 통한 salt 발급
3. 평문 비밀번호와 salt를 파라미터로한 passwordAuth(평문 비밀번호, salt)로 암호화 비밀번호 발급
```
#### 카카오 로그인이 수행되는 전체 프로젝트는 외부 공개 불가 프로젝트임을 밝혀 드립니다.

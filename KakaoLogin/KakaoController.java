import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.ssafy.happyhouse.service.KakaoService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/{URL}")
public class KakaoController {

	@Autowired
	private KakaoService kakaoService;

	@GetMapping("/login/{code}")
	@ApiOperation(value = "카카오 로그인 토큰 발급 및 회원 정보 등록", response = String.class)
	public ResponseEntity<String> kakaoLogin(@PathVariable String code)
			throws ParseException, SQLException, NoSuchAlgorithmException {
		// REST API 호출이후 응답을 받을 때까지 기다리는 동기 방식(스프링 3부터 지원)
		RestTemplate restTemplate = new RestTemplate();

		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		// 카카오 로그인 토큰 발급 과정
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "발급받은 클라이언트 REST API 키 입력");
		params.add("redirect_uri", "http://localhost:8080/{URL}");
		params.add("code", code);
		params.add("client_secret", "je2ChvXuq9C9FqkzqMe8OMcPUaZiROW6");

		HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<MultiValueMap<String, String>>(params,
				headers);

		// 정상적 수행
		ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST,
				kakaoRequest, String.class);
		// JSON 파싱
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(response.getBody().toString());

		// 발급받은 토큰을 통해 사용자 조회
		HttpHeaders token_access = new HttpHeaders();
		String token = (String) jsonObject.get("access_token");
		token_access.add("Authorization", "Bearer " + token);
		token_access.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<HttpHeaders> kakaoProfileRequest = new HttpEntity<>(token_access);

		ResponseEntity<String> profileResponse = restTemplate.exchange("https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST, kakaoProfileRequest, String.class);

		// 사용자 정보 확인 완료
		jsonObject = (JSONObject) parser.parse(profileResponse.getBody().toString());
		System.out.println("jsonObjct : " + jsonObject);
		HttpHeaders kakaoLoginHeaders = new HttpHeaders();
		String userid = String.valueOf(jsonObject.get("id"));
		kakaoLoginHeaders.add("userid", userid);
		jsonObject = (JSONObject) parser.parse(jsonObject.get("kakao_account").toString());
		jsonObject = (JSONObject) parser.parse(jsonObject.get("profile").toString());
		String name = (String) jsonObject.get("nickname");
		kakaoLoginHeaders.add("token", token);

		// 가입된 이력이 있음
		if (kakaoService.checkMember(userid) > 0) {
			System.out.println("가입된 이력이 있습니다.");
		} else {
			System.out.println("가입된 이력이 없습니다.");
			if (kakaoService.registMember(userid, name) > 0) {
				System.out.println("가입 왼료 되었습니다.");
			}

		}
		System.out.println(kakaoLoginHeaders.toString());
		return new ResponseEntity<String>("success", kakaoLoginHeaders, HttpStatus.OK);

	}

}

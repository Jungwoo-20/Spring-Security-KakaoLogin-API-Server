import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.model.MemberDto;
import com.ssafy.happyhouse.model.mapper.MemberMapper;
import com.ssafy.happyhouse.security.PasswordAuth;

@Service
public class KakaoServiceImpl implements KakaoService {

	@Autowired
	private MemberMapper memberMapper;
	
	private PasswordAuth auth;
	
	@Override
	public int checkMember(String id) throws SQLException {
		// TODO Auto-generated method stub
		return memberMapper.kakaoCheckId(id);
	}

	@Override
	public int registMember(String id, String name) throws SQLException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		MemberDto memberDto = new MemberDto();
		memberDto.setId(id);
		memberDto.setName(name);
		auth = PasswordAuth.getInstance();
		memberDto.setSalt_value(auth.saltValueIssued());
		memberDto.setPw(auth.passwordAuth(memberDto.getId(), memberDto.getSalt_value()));
		memberDto.setKakaoUser(1);
		return memberMapper.registMember(memberDto);
	}
	

}

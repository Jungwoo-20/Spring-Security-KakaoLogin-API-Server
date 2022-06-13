import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public interface KakaoService {
	public int checkMember(String id) throws SQLException;

	public int registMember(String id, String name) throws SQLException, NoSuchAlgorithmException;
}

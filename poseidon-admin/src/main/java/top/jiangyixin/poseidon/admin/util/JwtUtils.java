package top.jiangyixin.poseidon.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT 工具类
 * @version 1.0
 * @author jiangyixin
 * @date 2020/12/30 下午4:43
 */
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	/**
	 * 生成 JWT
	 * @param key           密匙
	 * @param subject       主题
	 * @param claims        Claims Payload
	 * @param ttMillis      过期时间，-1为永不过期
	 * @return              jwt
	 */
	public static String createJwt(String key, String subject,
	                               Map<String, Object> claims, long ttMillis) {
		//指定签名的时候使用的签名算法，也就是header那部分
		SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
		
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		JwtBuilder builder = Jwts.builder();
		if (claims != null && claims.size() > 0) {
			builder.setClaims(claims);
		}
		builder
				// 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，
				// 主要用来作为一次性token,从而回避重放攻击。
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(now)
				.setSubject(subject)
				.signWith(algorithm, key);
		
		// 如果有过期时间则设置
		if (ttMillis > 0) {
			long expireMillis = nowMillis + ttMillis;
			Date expire = new Date(expireMillis);
			builder.setExpiration(expire);
		}
		return builder.compact();
	}
	
	/**
	 * 解析 JWT
	 * @param key           密匙
	 * @param token         token
	 * @return              Claims Payload
	 */
	public static Claims parseJwt(String key, String token) {
		try {
			return Jwts.parser()
					.setSigningKey(key)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			logger.warn("JWT token <{}> parse fail, {}", token, e.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) {
//		String token = JwtUtil.createJwt("12345", "user", null, 1);
//		System.out.println(token);
		String s = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlMWVjYjIyOS1jODUwLTRmMzgtOWYyYS1mMGI0MWZlNjJkZmUiLCJpYXQiOjE2MDkzMTg4MjYsInN1YiI6InVzZXIiLCJleHAiOjE2MDkzMTg4MjZ9.W5vHqZilG_XxxiKJqN6dRY-ZJNzKLx32OmJc7Rd_eyg";
		Claims claims = JwtUtils.parseJwt("12345", s);
		if (claims == null) {
			System.out.println("失败");
		} else {
			System.out.println(claims.getExpiration());
			
		}
	}
}

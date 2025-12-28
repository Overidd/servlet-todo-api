package api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

  private static final String SECRET = "miSuperSecreto123";
  private static final long EXPIRATION_TIME = 1000 * 60 * 60;

  private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

  public static String generateToken(int userId, String email) {
    return JWT.create()
        .withClaim("id", userId)
        .withSubject(email)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(ALGORITHM);
  }

  public static int getUserId(String token) throws JWTVerificationException {
    DecodedJWT jwt = JWT.require(ALGORITHM)
        .build()
        .verify(token);

    return jwt.getClaim("id").asInt();
  }

  public static String getEmail(String token) throws JWTVerificationException {
    DecodedJWT jwt = JWT.require(ALGORITHM)
        .build()
        .verify(token);

    return jwt.getSubject();
  }
}

package api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

  private static final String SECRET = "miSuperSecreto123"; // cambiar por algo seguro
  private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

  public static String generateToken(String email) {
    return JWT.create()
        .withSubject(email)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(Algorithm.HMAC256(SECRET));
  }

  public static String validateToken(String token) throws JWTVerificationException {
    DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET))
        .build()
        .verify(token);
    return jwt.getSubject();
  }
}

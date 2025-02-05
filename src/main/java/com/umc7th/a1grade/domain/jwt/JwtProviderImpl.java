package com.umc7th.a1grade.domain.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.umc7th.a1grade.domain.auth.exception.AuthHandler;
import com.umc7th.a1grade.domain.auth.exception.status.AuthErrorStatus;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProviderImpl implements JwtProvider {
  private final Key key;
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 2;

  public JwtProviderImpl(@Value("${spring.jwt.secret}") String secretKey) {
    byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  @Override
  public String createAccessToken(String socialId, boolean idProfileComplete) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(socialId)
        .setId(String.valueOf(socialId))
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
        .claim("idProfileComplete", idProfileComplete)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public long getExpiration(String accessToken) {
    Claims claims =
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(accessToken)
            .getBody();

    Date expiration = claims.getExpiration();
    long now = System.currentTimeMillis();
    return expiration.getTime() - now;
  }

  private Key getSigningKey() {
    return key;
  }

  public String createRefreshToken(String socialId) {
    Date now = new Date();
    return Jwts.builder()
        .setSubject(socialId)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new AuthHandler(AuthErrorStatus.EXPIRED_TOKEN);
    } catch (UnsupportedJwtException e) {
      throw new AuthHandler(AuthErrorStatus.UNSUPPORTED_TOKEN);
    } catch (MalformedJwtException e) {
      throw new AuthHandler(AuthErrorStatus.MALFORMED_TOKEN);
    } catch (io.jsonwebtoken.SignatureException e) {
      throw new AuthHandler(AuthErrorStatus.INVALID_SIGNATURE);
    } catch (IllegalArgumentException e) {
      throw new AuthHandler(AuthErrorStatus.ILLEGAL_ARGUMENT);
    }
  }

  public String extractSocialId(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}

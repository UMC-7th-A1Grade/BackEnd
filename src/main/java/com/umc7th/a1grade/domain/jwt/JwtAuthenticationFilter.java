package com.umc7th.a1grade.domain.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.umc7th.a1grade.domain.auth.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;
  private final TokenService tokenService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = resolveToken(request);

      if (token != null && tokenService.isBlacklisted(token)) {
        log.warn("블랙리스트에 등록된 토큰입니다.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("만료된 토큰입니다.");
        return;
      }

      if (token != null && jwtProvider.validateToken(token)) {
        String socialId = jwtProvider.extractSocialId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(socialId);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("SecurityContext에 '{}' 인증 정보를 저장했습니다.", socialId);
      }
    } catch (JwtException | IllegalArgumentException e) {
      log.error("JWT 검증 실패 : {}", e.getMessage());
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("JWT 인증 실패");
      return;
    }
    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }
}

package com.emmanuelc.plagiarismchecker.security;

import com.emmanuelc.plagiarismchecker.domain.models.BlacklistedToken;
import com.emmanuelc.plagiarismchecker.repository.BlacklistedTokenRepo;
import com.emmanuelc.plagiarismchecker.util.AppConstants;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenUtil {
    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.validity}")
    private long tokenValidityInMilliSeconds;

    private final MyUserDetailsServices userDetailsService;

    private final BlacklistedTokenRepo blacklistedTokenRepo;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    protected String generateToken(final String subject, final Long validityPeriod) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date(System.currentTimeMillis());
        Date validity = new Date(now.getTime() + validityPeriod);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createToken(final String username) {
        return generateToken(username, tokenValidityInMilliSeconds);
    }

    public String resolveToken(final HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstants.AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(AppConstants.JWT_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Method to validate token
     * @param token token to validate
     * @return true if valid token else false
     */
    public boolean validateToken(final String token) {
        // this also checks if the token is blacklisted
        final BlacklistedToken blacklistedToken = blacklistedTokenRepo.findByToken(token);
        if (blacklistedToken != null) return false;
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(final String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(final String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private Claims getAllClaims(final String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getTokenExpiration(final String token) {
        return getAllClaims(token).getExpiration();
    }
}

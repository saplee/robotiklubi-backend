package ee.taltech.iti0302.robotiklubi.tokens;

import ee.taltech.iti0302.robotiklubi.exception.TokenParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Slf4j
public class TokenBuilder {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private TokenBuilder() {}

    public static String createToken(Long id, Integer authLevel, Long validUntil) {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
                .claim("id", id)
                .claim("auth", authLevel)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(issuedAt.plusSeconds(validUntil)))
                .signWith(key)
                .compact();
    }

    public static Claims fromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (RuntimeException e) {
            // TODO: the ErrorHandler cannot catch this for some reason
            throw new TokenParseException("Token parsing failed", e);
        }
    }

}

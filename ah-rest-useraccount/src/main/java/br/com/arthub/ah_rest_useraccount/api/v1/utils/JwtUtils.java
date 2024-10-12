package br.com.arthub.ah_rest_useraccount.api.v1.utils;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMillis;

    private long _30_MIN_IN_MILI = 1800000L;

    /* Claims. */
    public static final String CL_PASSWORD_RESET = "password_reset";
    public static final String CL_CONFIRM_EMAIL = "confirm_email";

    public String generatePasswordResetToken(String email) {
        return generateToken(email, _30_MIN_IN_MILI, Map.of("purpose", CL_PASSWORD_RESET));
    }
    
    public String generateConfirmationEmailToken(String email) {
        return generateToken(email, _30_MIN_IN_MILI, Map.of("purpose", CL_CONFIRM_EMAIL));
    }

    public String generateAuthToken(String email) {
        return generateToken(email, jwtExpirationInMillis, null);
    }

    private String generateToken(String subject, long expiresAt, Map<String, String> claims) {
        var jwtBuilder = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresAt));

        if (claims != null) {
            claims.forEach(jwtBuilder::withClaim);
        }

        return jwtBuilder.sign(Algorithm.HMAC256(jwtSecret));
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public boolean isClaimValid(String token, String claim) {
        return claim.equals(extractClaim(token, "purpose"));
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return decodeToken(token).getExpiresAt().before(new Date());
    }

    public String extractClaim(String token, String claimKey) {
        return decodeToken(token).getClaim(claimKey).asString();
    }

    private DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
        return verifier.verify(token);
    }

    public Date getExpiresAt(String token) {
        return decodeToken(token).getExpiresAt();
    }
}

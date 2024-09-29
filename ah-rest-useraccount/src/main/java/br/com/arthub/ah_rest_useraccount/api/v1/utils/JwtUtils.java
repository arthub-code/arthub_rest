package br.com.arthub.ah_rest_useraccount.api.v1.utils;

import java.util.Date;

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

	public String generateTokenToAccountConfirmation(String email) {
		// Cria o token JWT
		return JWT.create()
				.withSubject(email) // O subject será o email do usuário
				.withExpiresAt(new Date(System.currentTimeMillis() + _30_MIN_IN_MILI)) 
				.sign(Algorithm.HMAC256(jwtSecret));
	}

	// Valida o token JWT
	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	// Extrai o username do token JWT
	public String extractUsername(String token) {
		return decodeToken(token).getSubject();
	}

	// Verifica se o token está expirado
	public boolean isTokenExpired(String token) {
		return decodeToken(token).getExpiresAt().before(new Date());
	}

	// Decodifica o token JWT
	private DecodedJWT decodeToken(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
		return verifier.verify(token);
	}
}

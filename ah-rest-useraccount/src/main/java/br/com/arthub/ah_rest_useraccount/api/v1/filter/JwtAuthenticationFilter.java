package br.com.arthub.ah_rest_useraccount.api.v1.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.arthub.ah_rest_useraccount.api.v1.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;

        // Verifica se o header Authorization contém um token JWT
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        // Verifica se o token é válido
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Verifica se o token é válido
                if (jwtUtil.validateToken(jwt, jwtUtil.extractUsername(jwt))) {
                    // Cria o token de autenticação
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            jwtUtil.extractUsername(jwt), null, null); // Não precisa do UserDetails aqui

                    // Configura os detalhes de autenticação do contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                // Tratamento de exceções, caso o token seja inválido
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token");
                return;
            }
        }

        // Prossegue com o filtro da requisição
        chain.doFilter(request, response);
    }
}

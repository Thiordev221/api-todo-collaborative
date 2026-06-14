package sn.thiordev221.app.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.service.implementations.JwtService;
import sn.thiordev221.app.service.implementations.UtilisateurDetailsService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UtilisateurDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
        }

        final String token = authHeader.substring(7);

        try {

            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken
                            = new UsernamePasswordAuthenticationToken(userDetails,
                                    null, // credentials null — déjà authentifié
                                    userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);
                }
            }

        } catch (ExpiredJwtException e) {
            // Token expiré → 401, on ne peuple pas le contexte
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token expiré");
            return;

        } catch (JwtException e) {
            // Token malformé, signature invalide, etc.
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Token invalide");
            return;

        } catch (UsernameNotFoundException e) {
            // Utilisateur supprimé après émission du token
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Utilisateur introuvable");
            return;
        }

        // 11. Passer à la suite de la chaîne dans tous les cas
        filterChain.doFilter(request, response);
    }

    // Retourne une réponse JSON propre au lieu d'une page HTML d'erreur
    private void sendError(HttpServletResponse response,
            int status,
            String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                String.format("""
                {
                  "status": %d,
                  "error": "%s"
                }
                """, status, message)
        );
    }
}

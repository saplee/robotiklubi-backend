package ee.taltech.iti0302.robotiklubi.security;

import ee.taltech.iti0302.robotiklubi.tokens.TokenBuilder;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> jwt = getToken(request);
        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims tokenBody = TokenBuilder.fromToken(jwt.get());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(buildAuthToken(tokenBody));

        filterChain.doFilter(request, response);
    }

    private Optional<String> getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.replace("Bearer ", "");
            return Optional.of(jwt);
        }
        return Optional.empty();
    }


    private UsernamePasswordAuthenticationToken buildAuthToken(Claims tokenBody) {
        return switch (tokenBody.get("auth", Integer.class)) {
            case 0 -> new UsernamePasswordAuthenticationToken(tokenBody.get("id", Long.class), null,
                    List.of(new SimpleGrantedAuthority("USER")));
            case 1 -> new UsernamePasswordAuthenticationToken(tokenBody.get("id", Long.class), null,
                    List.of(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("MEMBER")));
            case 2 -> new UsernamePasswordAuthenticationToken(tokenBody.get("id", Long.class), null,
                    List.of(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("FORMER_MEMBER")));
            case 3 -> new UsernamePasswordAuthenticationToken(tokenBody.get("id", Long.class), null,
                    List.of(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("MEMBER"),
                            new SimpleGrantedAuthority("FORMER_MEMBER"),
                            new SimpleGrantedAuthority("MANAGEMENT")));
            default -> null;
        };
    }
}

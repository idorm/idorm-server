package idorm.idormServer.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT를 받아옵니다.
        String token = jwtAuthenticationProvider.resolveToken(request);

        // 유효한 토큰인지 확인합니다.
        if(token != null && jwtAuthenticationProvider.validateToken(token)){
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);

            // SecurityContext에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}

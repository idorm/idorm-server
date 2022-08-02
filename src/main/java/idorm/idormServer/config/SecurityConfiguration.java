package idorm.idormServer.config;

import idorm.idormServer.jwt.JwtAuthenticationFilter;
import idorm.idormServer.jwt.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // REST API만을 고려하여 기본 설정은 해제
                .csrf().disable() // csrf 보안 토큰 disable 처리
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않는다.
                .and()
                .exceptionHandling()
                .and()
                .authorizeHttpRequests((authz) -> authz // 요청에 대한 사용권한 체크
                        .anyRequest().authenticated()
//                        .antMatchers("/email").permitAll()
//                        .antMatchers("/verifyCode/**").permitAll()
//                        .antMatchers("/admin/**").hasRole("ADMIN")
//                        .antMatchers("/member/**").hasRole("USER")
                        .anyRequest().permitAll()
                        .and()
                        .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class)

//                 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

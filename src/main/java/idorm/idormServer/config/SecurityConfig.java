package idorm.idormServer.config;

import idorm.idormServer.exceptions.CustomAccessDeniedHandler;
import idorm.idormServer.exceptions.CustomAuthenticationEntryPoint;
import idorm.idormServer.jwt.JwtAuthenticationFilter;
import idorm.idormServer.jwt.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // REST API만을 고려하여 기본 설정은 해제
                .csrf().disable() // csrf 보안 토큰 disable 처리
//                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않는다.
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .antMatchers("/email").permitAll()
                .antMatchers("/verifyCode/**").permitAll()

                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/member/**").hasRole("USER")
//                .antMatchers("/myInfo/**").hasRole("USER")
                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);

                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean에 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

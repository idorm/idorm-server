package idorm.idormServer.config;

import idorm.idormServer.common.filter.CustomJwtAuthenticationFilter;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.common.handler.CustomAccessDeniedHandler;
import idorm.idormServer.common.handler.CustomAuthenticationEntryPointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/auth";
    public static final String API_ROOT_URL_V1 = "/api/v1";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomAuthenticationEntryPointHandler authenticationEntryPointHandler;
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
                        API_ROOT_URL_V1 + AUTHENTICATION_URL +"/**").permitAll()
                .antMatchers("/test/**", API_ROOT_URL_V1 + "/calendar/**", API_ROOT_URL_V1 +"/calendars").permitAll() // TODO: 운영 서버 삭제
                .antMatchers(API_ROOT_URL_V1 + "/admin/**").hasRole("ADMIN")
                .antMatchers(API_ROOT_URL_V1 + "/member/**").hasRole("USER")
                .anyRequest().denyAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler)

                .and()
                .addFilterBefore(new CustomJwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }
}

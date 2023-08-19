package idorm.idormServer.config;

import idorm.idormServer.exception.CustomAccessDeniedHandler;
import idorm.idormServer.exception.CustomAuthenticationEntryPointHandler;
import idorm.idormServer.auth.CustomJwtAuthenticationFilter;
import idorm.idormServer.auth.JwtTokenProvider;
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
public class SecurityConfiguration extends WebSecurityConfigurerAdapter  {

    public static final String AUTHENTICATION_HEADER_NAME = "X-AUTH-TOKEN";
    public static final String AUTHENTICATION_URL = "/api/auth";
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
                .antMatchers("/swagger-ui/**", "/swagger-resources/**").denyAll()
                .antMatchers(AUTHENTICATION_URL, "/email/**", "/verifyCode/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/member/**", API_ROOT_URL_V1 + "/member/**").hasRole("USER")
                .anyRequest().permitAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler)

                .and()
                .addFilterBefore(new CustomJwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }
}

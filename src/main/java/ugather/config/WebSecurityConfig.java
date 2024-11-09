package ugather.config;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import ugather.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/users/signin").permitAll()
            .requestMatchers("/users/signup").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/v3/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
    );

    http.exceptionHandling(exception -> exception.accessDeniedPage("/login"));

    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    return http.build();
}

@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
            .requestMatchers("/v2/api-docs")
            .requestMatchers("/swagger-resources/**")
            .requestMatchers("/swagger-ui.html")
            .requestMatchers("/configuration/**")
            .requestMatchers("/webjars/**")
            .requestMatchers("/public")
            .requestMatchers("/h2-console/**")
            .requestMatchers("/v3/api-docs")
            .requestMatchers("/v3/api-docs.yaml");
}

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
}
package hu.okrim.productreviewappcomplete.security;

import hu.okrim.productreviewappcomplete.repository.UserRepository;
import hu.okrim.productreviewappcomplete.security.filter.AuthenticationFilter;
import hu.okrim.productreviewappcomplete.security.filter.ExceptionHandlerFilter;
import hu.okrim.productreviewappcomplete.security.filter.JWTAuthorizationFilter;
import hu.okrim.productreviewappcomplete.security.manager.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final CustomAuthenticationManager customAuthenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(CustomAuthenticationManager customAuthenticationManager, UserRepository userRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager, userRepository);
        authenticationFilter.setFilterProcessesUrl("/api/authenticate");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000", "http://161.97.146.75:3000", "https://product-review.app"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/country/all").permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/security/session-second").permitAll()
                        // Define role-based access control
                        // Reviews can be modified by both users and admins
                        // Security endpoint is required for the frontend to work
                        .requestMatchers("/api/review-head/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/security/**").hasAnyRole("USER", "ADMIN")
                        // GET methods should work for anyone
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
                        // Any other request is only accessible to admins
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}

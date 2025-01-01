package hu.okrim.productreviewappcomplete.security;

import hu.okrim.productreviewappcomplete.security.filter.AuthenticationFilter;
import hu.okrim.productreviewappcomplete.security.filter.ExceptionHandlerFilter;
import hu.okrim.productreviewappcomplete.security.filter.JWTAuthorizationFilter;
import hu.okrim.productreviewappcomplete.security.manager.CustomAuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    @Autowired
    CustomAuthenticationManager customAuthenticationManager;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
        authenticationFilter.setFilterProcessesUrl("/authenticate");
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, SecurityConstants.REGISTER_PATH).permitAll()
                    .requestMatchers(HttpMethod.GET, "/country/all").permitAll()
                    // Define role-based access control
                    // Reviews can be modified by both users and admins
                    // Security endpoint is required for the frontend to work
                    .requestMatchers("/review-head/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/security/**").hasAnyRole("USER", "ADMIN")
                    // GET methods should work for anyone
                    .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN")
                    // Any other request is only accessible to admins
                    .requestMatchers("/**").hasRole("ADMIN")
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

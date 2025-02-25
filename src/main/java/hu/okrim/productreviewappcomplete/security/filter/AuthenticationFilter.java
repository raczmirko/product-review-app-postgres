package hu.okrim.productreviewappcomplete.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.exception.UserIsNotActiveException;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.repository.UserRepository;
import hu.okrim.productreviewappcomplete.security.SecurityConstants;
import hu.okrim.productreviewappcomplete.security.manager.CustomAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserDTO userDto = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);

            Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
            if (optionalUser.isEmpty()) {
                throw new BadCredentialsException("Invalid username or password.");
            }

            User user = optionalUser.get();

            if (!user.getIsActive()) {
                throw new UserIsNotActiveException("User account is inactive. Please contact support.");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDto.getUsername(), userDto.getPassword()
            );

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException("Error reading login request", e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = JWT.create()
                .withSubject(authResult.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));

        response.addHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }
}

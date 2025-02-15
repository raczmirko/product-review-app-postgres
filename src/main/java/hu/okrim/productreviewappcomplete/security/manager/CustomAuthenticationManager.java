package hu.okrim.productreviewappcomplete.security.manager;

import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private UserService userServiceImpl;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userServiceImpl.findByUsername(authentication.getName());
        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("You provided an incorrect password.");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName())));
    }
}

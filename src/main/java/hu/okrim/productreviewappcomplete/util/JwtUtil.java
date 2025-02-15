package hu.okrim.productreviewappcomplete.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import hu.okrim.productreviewappcomplete.security.SecurityConstants;
import hu.okrim.productreviewappcomplete.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Autowired
    UserService userService;

    public String extractUserFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String returnUser = null;
        if (header != null && header.startsWith(SecurityConstants.BEARER)) {
            String token = header.replace(SecurityConstants.BEARER, "");
            returnUser = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
                    .build()
                    .verify(token)
                    .getSubject();
        }
        return returnUser;
    }

    public String extractUserRoleFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String userRole = null;
        if (header != null && header.startsWith(SecurityConstants.BEARER)) {
            String token = header.replace(SecurityConstants.BEARER, "");
            String username = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
                    .build()
                    .verify(token)
                    .getSubject();
            userRole = userService.getUserRole(username);
        }
        return userRole;
    }
}

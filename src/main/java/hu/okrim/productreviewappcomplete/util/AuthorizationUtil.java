package hu.okrim.productreviewappcomplete.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtil {

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> checkAuthorization(HttpServletRequest request, String requestUsername) {
        String tokenUsername = jwtUtil.extractUserFromToken(request);
        if (!requestUsername.equals(tokenUsername)) {
            String errorMessage = "UNAUTHORIZED: The entity you are trying to modify belongs to another user.";
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }
        return null;
    }
}
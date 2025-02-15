package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.security.SecurityConstants;
import hu.okrim.productreviewappcomplete.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    UserService userService;

    @GetMapping("/session-second")
    public ResponseEntity<Integer> getSessionDuration() {
        Integer timeout = SecurityConstants.TOKEN_EXPIRATION / 1000;
        return new ResponseEntity<>(timeout, HttpStatus.OK);
    }

    @GetMapping("/current-user-role")
    public ResponseEntity<String> getCurrentUserRole(@RequestParam String username) {
        String role = userService.getUserRole(username);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}

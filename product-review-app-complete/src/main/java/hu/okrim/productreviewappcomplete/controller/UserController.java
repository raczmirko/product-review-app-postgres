package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.mapper.UserMapper;
import hu.okrim.productreviewappcomplete.model.Role;
import hu.okrim.productreviewappcomplete.model.RoleType;
import hu.okrim.productreviewappcomplete.service.CountryService;
import hu.okrim.productreviewappcomplete.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CountryService countryService;

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id).getUsername(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody UserDTO user) {
        user.setIsActive(true);
        user.setRole(new Role(RoleType.USER));
        user.setRegistrationDate(ZonedDateTime.now());
        userService.save(UserMapper.mapToUser(user));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.mapper.UserMapper;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.service.CountryService;
import hu.okrim.productreviewappcomplete.service.RoleService;
import hu.okrim.productreviewappcomplete.service.UserService;
import hu.okrim.productreviewappcomplete.specification.UserSpecificationBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CountryService countryService;
    @Autowired
    RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id).getUsername(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(@RequestParam(value = "searchText", required = false) String searchText,
                                                  @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                  @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                  @RequestParam("pageSize") Integer pageSize,
                                                  @RequestParam("pageNumber") Integer pageNumber,
                                                  @RequestParam("orderByColumn") String orderByColumn,
                                                  @RequestParam("orderByDirection") String orderByDirection) {

        UserSpecificationBuilder<User> userSpecificationBuilder = new UserSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> userSpecificationBuilder.withId(searchText);
                case "username" -> userSpecificationBuilder.withUsername(searchText);
                case "active" -> userSpecificationBuilder.withIsActive(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                userSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<User> specification = userSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<User> userPage = userService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody UserDTO user) {
        user.setIsActive(true);
        user.setRole(roleService.getUserRole());
        user.setRegistrationDate(ZonedDateTime.now());
        userService.save(UserMapper.mapToUser(user));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

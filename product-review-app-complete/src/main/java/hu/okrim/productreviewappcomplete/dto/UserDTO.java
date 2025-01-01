package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message =  "Username cannot be blank.")
    private String username;
    @NotBlank(message =  "Password cannot be blank.")
    private String password;
    private Country country;
    private ZonedDateTime registrationDate;
    private Role role;
    private Boolean isActive;
}

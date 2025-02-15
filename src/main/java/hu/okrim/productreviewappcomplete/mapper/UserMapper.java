package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.model.User;

public class UserMapper {
    public static UserDTO mapToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getCountry(),
                user.getRegistrationDate(),
                user.getRole(),
                user.getIsActive()
        );
    }

    public static User mapToUser(UserDTO userDTO) {
        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getCountry(),
                userDTO.getRegistrationDate(),
                userDTO.getRole(),
                userDTO.getIsActive()
        );
    }
}

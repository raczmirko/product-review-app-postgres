package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMostActiveUserDTO {
    private User user;
    private Long reviewCount;
}

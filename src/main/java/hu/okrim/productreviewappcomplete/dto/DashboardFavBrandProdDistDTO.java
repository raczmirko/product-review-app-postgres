package hu.okrim.productreviewappcomplete.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardFavBrandProdDistDTO {
    private String range;
    private Double percentage;
}

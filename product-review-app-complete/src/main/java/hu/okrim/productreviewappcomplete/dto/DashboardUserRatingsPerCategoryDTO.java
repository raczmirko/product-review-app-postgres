package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardUserRatingsPerCategoryDTO {
    private Category category;
    private Double reviewPercentage;
    private Long reviewAmount;
}

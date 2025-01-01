package hu.okrim.productreviewappcomplete.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardReviewByMonthDTO {
    private String month;
    private Long reviewCount;
}

package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardUserBestRatedProductsDTO {
    private Product product;
    private Double scoreAverage;
    private Long rank;
}

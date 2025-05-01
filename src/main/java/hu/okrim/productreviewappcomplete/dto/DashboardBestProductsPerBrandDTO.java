package hu.okrim.productreviewappcomplete.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardBestProductsPerBrandDTO {
    private String brand;
    private String article;
    private Double average;
}

package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Packaging;
import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import hu.okrim.productreviewappcomplete.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private Article article;
    private Packaging packaging;
    private List<ProductImage> productImages;
    private List<ProductCharacteristicValue> productCharacteristicValues;
}

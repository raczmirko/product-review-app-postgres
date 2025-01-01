package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCharacteristicValueDTO {
    private Long id;
    private Product product;
    private Characteristic characteristic;
    private String value;
}

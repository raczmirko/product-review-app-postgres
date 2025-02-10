package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.ProductDTO;
import hu.okrim.productreviewappcomplete.model.Product;

public class ProductMapper {
    public static ProductDTO mapToProductDTO (Product product){
        return new ProductDTO(
            product.getId(),
            product.getArticle(),
            product.getPackaging(),
            product.getProductImages(),
            product.getProductCharacteristicValues()
        );
    }

    public static Product mapToProduct (ProductDTO productDTO){
        return new Product(
            productDTO.getId() != null ? productDTO.getId() : null,
            productDTO.getArticle(),
            productDTO.getPackaging(),
            productDTO.getProductImages(),
            productDTO.getProductCharacteristicValues()
        );
    }
}

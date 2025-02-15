package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.ProductImage;

public interface ProductImageService {
    ProductImage findById(Long id);

    void deleteById(Long id);

    void save(ProductImage productImage);
}

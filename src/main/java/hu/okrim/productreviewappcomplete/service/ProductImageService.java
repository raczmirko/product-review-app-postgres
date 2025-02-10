package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductImageService {
    ProductImage findById(Long id);
    void deleteById(Long id);
    void save (ProductImage productImage);
}

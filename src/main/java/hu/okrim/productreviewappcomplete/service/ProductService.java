package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {
    Product findById(Long id);
    void deleteById(Long id);
    void save (Product product);
    List<Product> findAll();
    Page<Product> findAllBySpecification(Specification<Product> specification, Pageable pageable);
    List<Product> findProductsByArticleId(Long articleId);
}

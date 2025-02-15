package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Product.class));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAllBySpecification(Specification<Product> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> findProductsByArticleId(Long articleId) {
        return productRepository.findByArticleId(articleId);
    }
}

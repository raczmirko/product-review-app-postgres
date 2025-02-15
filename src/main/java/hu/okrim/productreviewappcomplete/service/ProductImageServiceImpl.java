package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.ProductImage;
import hu.okrim.productreviewappcomplete.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    @Autowired
    ProductImageRepository productImageRepository;

    @Override
    public ProductImage findById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, ProductImage.class));
    }

    @Override
    public void deleteById(Long id) {
        productImageRepository.deleteById(id);
    }

    @Override
    public void save(ProductImage productImage) {
        productImageRepository.save(productImage);
    }
}

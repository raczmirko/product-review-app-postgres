package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductCharacteristicsValueService {
    List<ProductCharacteristicValue> findByProductId(Long id);

    List<ProductCharacteristicValue> findByCharacteristicId(Long id);

    void deleteById(Long id);

    void save(ProductCharacteristicValue productCharacteristicValue);

    List<ProductCharacteristicValue> findAll();

    Page<ProductCharacteristicValue> findAllBySpecification(Specification<ProductCharacteristicValue> specification, Pageable pageable);

    ProductCharacteristicValue findByProductAndCharacteristic(Product product, Characteristic characteristic);
}

package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCharacteristicValueRepository extends JpaRepository<ProductCharacteristicValue, Long> {
    List<ProductCharacteristicValue> findByProductId(Long productId);

    List<ProductCharacteristicValue> findByCharacteristicId(Long characteristicId);

    Page<ProductCharacteristicValue> findAll(Specification<ProductCharacteristicValue> specification, Pageable pageable);

    ProductCharacteristicValue findByProductAndCharacteristic(Product product, Characteristic characteristic);
}

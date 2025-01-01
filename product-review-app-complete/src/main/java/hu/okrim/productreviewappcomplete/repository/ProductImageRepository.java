package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}

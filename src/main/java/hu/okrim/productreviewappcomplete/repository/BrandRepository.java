package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findAllByOrderByName();

    Page<Brand> findAll(Specification<Brand> specification, Pageable pageable);
}

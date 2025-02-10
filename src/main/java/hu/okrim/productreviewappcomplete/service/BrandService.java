package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BrandService {
    Brand findById(Long id);
    void deleteById(Long id);
    void save(Brand brand);
    List<Brand> findAll();
    Page<Brand> findAllBySpecification(Specification<Brand> specification, Pageable pageable);
}

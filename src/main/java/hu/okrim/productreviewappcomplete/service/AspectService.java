package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Aspect;
import hu.okrim.productreviewappcomplete.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface AspectService {
    Aspect findById(Long id);

    void deleteById(Long id);

    void save(Aspect aspect);

    List<Aspect> findAll();

    Page<Aspect> findAllBySpecification(Specification<Aspect> specification, Pageable pageable);

    List<Aspect> findByCategory(Category category);
}

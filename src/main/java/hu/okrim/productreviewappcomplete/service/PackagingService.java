package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Packaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PackagingService {
    Packaging findById(Long id);
    void deleteById(Long id);
    void save (Packaging brand);
    List<Packaging> findAll();
    Page<Packaging> findAllBySpecification(Specification<Packaging> specification, Pageable pageable);
}

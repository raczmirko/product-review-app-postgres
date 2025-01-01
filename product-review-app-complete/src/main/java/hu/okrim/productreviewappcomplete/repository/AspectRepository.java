package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Aspect;
import hu.okrim.productreviewappcomplete.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AspectRepository extends JpaRepository<Aspect, Long> {
    Page<Aspect> findAll(Specification<Aspect> specification, Pageable pageable);
    List<Aspect> findAllByCategory(Category category);
}

package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Packaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagingRepository extends JpaRepository<Packaging, Long> {
    Page<Packaging> findAll(Specification<Packaging> specification, Pageable pageable);
}

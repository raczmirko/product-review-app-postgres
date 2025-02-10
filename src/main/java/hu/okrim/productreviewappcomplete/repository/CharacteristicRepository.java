package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    Page<Characteristic> findAll(Specification<Characteristic> specification, Pageable pageable);
}

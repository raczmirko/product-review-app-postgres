package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CharacteristicService {
    List<Characteristic> findAll ();
    Characteristic findById(Long id);
    void deleteById(Long id);
    void save(Characteristic characteristic);
    Page<Characteristic> findAllBySpecification(Specification<Characteristic> specification, Pageable pageable);
}

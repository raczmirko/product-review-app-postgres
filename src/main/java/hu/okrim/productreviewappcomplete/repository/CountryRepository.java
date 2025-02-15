package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    List<Country> findByOrderByNameAsc();

    Page<Country> findAll(Specification<Country> specification, Pageable pageable);

    Country findByCountryCode(String countryCode);
}

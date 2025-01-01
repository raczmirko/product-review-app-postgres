package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CountryService {
    public Country findByCountryCode(String countryCode);
    public void save(Country country);
    public void deleteByCountryCode(String countryCode);
    List<Country> findAll();
    Page<Country> findAllBySpecification(Specification<Country> specification, Pageable pageable);
}

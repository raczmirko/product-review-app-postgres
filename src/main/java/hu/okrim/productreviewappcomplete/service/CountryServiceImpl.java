package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    CountryRepository countryRepository;

    @Override
    public Country findByCountryCode(String countryCode) {
        return countryRepository.findByCountryCode(countryCode);
    }

    @Override
    public void save(Country country) {
        countryRepository.save(country);
    }

    @Override
    public void deleteByCountryCode(String countryCode) {
        countryRepository.deleteById(countryCode);
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findByOrderByNameAsc();
    }

    @Override
    public Page<Country> findAllBySpecification(Specification<Country> specification, Pageable pageable) {
        return countryRepository.findAll(specification, pageable);
    }
}

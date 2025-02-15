package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Brand.class));
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAllByOrderByName();
    }

    @Override
    public Page<Brand> findAllBySpecification(Specification<Brand> specification, Pageable pageable) {
        return brandRepository.findAll(specification, pageable);
    }
}

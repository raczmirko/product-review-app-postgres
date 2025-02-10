package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Packaging;
import hu.okrim.productreviewappcomplete.repository.PackagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackagingServiceImpl implements PackagingService{
    @Autowired
    PackagingRepository packagingRepository;
    @Override
    public Packaging findById(Long id) {
        return packagingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Packaging.class));
    }

    @Override
    public void deleteById(Long id) {
        packagingRepository.deleteById(id);
    }

    @Override
    public void save(Packaging packaging) {
        packagingRepository.save(packaging);
    }

    @Override
    public List<Packaging> findAll() {
        return packagingRepository.findAll();
    }

    @Override
    public Page<Packaging> findAllBySpecification(Specification<Packaging> specification, Pageable pageable) {
        return packagingRepository.findAll(specification, pageable);
    }
}

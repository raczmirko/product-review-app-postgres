package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Aspect;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.repository.AspectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AspectServiceImpl implements AspectService {
    @Autowired
    AspectRepository aspectRepository;

    @Override
    public Aspect findById(Long id) {
        return aspectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Aspect.class));
    }

    @Override
    public void deleteById(Long id) {
        aspectRepository.deleteById(id);
    }

    @Override
    public void save(Aspect aspect) {
        aspectRepository.save(aspect);
    }

    @Override
    public List<Aspect> findAll() {
        return aspectRepository.findAll();
    }

    @Override
    public Page<Aspect> findAllBySpecification(Specification<Aspect> specification, Pageable pageable) {
        return aspectRepository.findAll(specification, pageable);
    }

    @Override
    public List<Aspect> findByCategory(Category category) {
        return aspectRepository.findAllByCategory(category);
    }
}

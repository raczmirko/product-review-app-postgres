package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Characteristic;
import hu.okrim.productreviewappcomplete.repository.CharacteristicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacteristicServiceImpl implements CharacteristicService {
    @Autowired
    CharacteristicRepository characteristicRepository;

    @Override
    public List<Characteristic> findAll() {
        return characteristicRepository.findAll();
    }

    @Override
    public Characteristic findById(Long id) {
        return characteristicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Characteristic.class));
    }

    @Override
    public void deleteById(Long id) {
        characteristicRepository.deleteById(id);
    }

    @Override
    public void save(Characteristic characteristic) {
        characteristicRepository.save(characteristic);
    }

    @Override
    public Page<Characteristic> findAllBySpecification(Specification<Characteristic> specification, Pageable pageable) {
        return characteristicRepository.findAll(specification, pageable);
    }
}

package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.WeakAspectOfMostPopularProductsView;
import hu.okrim.productreviewappcomplete.repository.WeakAspectOfMostPopularProductsViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeakAspectOfMostPopularProductsViewServiceImpl implements WeakAspectOfMostPopularProductsViewService {
    @Autowired
    WeakAspectOfMostPopularProductsViewRepository repository;
    @Override
    public List<WeakAspectOfMostPopularProductsView> findAll() {
        return repository.findAll();
    }
}

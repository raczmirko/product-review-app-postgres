package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerBrandView;
import hu.okrim.productreviewappcomplete.repository.MostPopularArticlesPerBrandViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MostPopularArticlesPerBrandViewServiceImpl implements MostPopularArticlesPerBrandViewService {
    @Autowired
    MostPopularArticlesPerBrandViewRepository mostPopularArticlesPerBrandViewRepository;

    @Override
    public List<MostPopularArticlesPerBrandView> findAll() {
        return mostPopularArticlesPerBrandViewRepository.findAll();
    }
}

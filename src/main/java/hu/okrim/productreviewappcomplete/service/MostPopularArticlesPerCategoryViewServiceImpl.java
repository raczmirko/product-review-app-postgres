package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerCategoryView;
import hu.okrim.productreviewappcomplete.repository.MostPopularArticlesPerCategoryViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MostPopularArticlesPerCategoryViewServiceImpl implements MostPopularArticlesPerCategoryViewService{
    @Autowired
    MostPopularArticlesPerCategoryViewRepository mostPopularArticlesPerCategoryViewRepository;

    @Override
    public List<MostPopularArticlesPerCategoryView> findAll() {
        return mostPopularArticlesPerCategoryViewRepository.findAll();
    }
}

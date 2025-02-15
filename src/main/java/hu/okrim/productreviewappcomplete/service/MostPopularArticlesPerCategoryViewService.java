package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerCategoryView;

import java.util.List;

public interface MostPopularArticlesPerCategoryViewService {
    List<MostPopularArticlesPerCategoryView> findAll();
}

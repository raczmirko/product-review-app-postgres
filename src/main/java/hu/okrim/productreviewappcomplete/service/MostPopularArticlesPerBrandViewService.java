package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerBrandView;

import java.util.List;

public interface MostPopularArticlesPerBrandViewService {
    List<MostPopularArticlesPerBrandView> findAll();
}

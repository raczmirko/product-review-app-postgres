package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.WeakAspectOfMostPopularProductsView;

import java.util.List;

public interface WeakAspectOfMostPopularProductsViewService {
    List<WeakAspectOfMostPopularProductsView> findAll();
}

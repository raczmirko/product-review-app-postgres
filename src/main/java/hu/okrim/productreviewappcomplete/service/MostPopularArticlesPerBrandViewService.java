package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.DashboardBestProductsPerBrandDTO;

import java.util.List;

public interface MostPopularArticlesPerBrandViewService {
    List<DashboardBestProductsPerBrandDTO> findAll();
}

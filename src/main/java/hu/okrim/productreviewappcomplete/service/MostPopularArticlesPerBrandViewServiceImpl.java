package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.DashboardBestProductsPerBrandDTO;
import hu.okrim.productreviewappcomplete.repository.MostPopularArticlesPerBrandViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MostPopularArticlesPerBrandViewServiceImpl implements MostPopularArticlesPerBrandViewService {
    @Autowired
    MostPopularArticlesPerBrandViewRepository mostPopularArticlesPerBrandViewRepository;

    @Override
    public List<DashboardBestProductsPerBrandDTO> findAll() {
        return mostPopularArticlesPerBrandViewRepository
                .findAll()
                .stream()
                .map(rating -> new DashboardBestProductsPerBrandDTO(
                        rating.getId().getBrand(),
                        rating.getId().getArticle(),
                        rating.getAverage()
                ))
                .collect(Collectors.toList());
    }
}

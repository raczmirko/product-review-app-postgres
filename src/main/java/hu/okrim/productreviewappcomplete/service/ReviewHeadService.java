package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.DashboardFavBrandProdDistDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserRatingsPerCategoryDTO;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ReviewHeadService {
    ReviewHead findById(ReviewHeadId id);
    Optional<ReviewHead> findByUserAndProduct(User user, Product product);
    void deleteById(ReviewHeadId id);
    void save (ReviewHead reviewHead);
    List<ReviewHead> findAll();
    Page<ReviewHead> findAllBySpecification(Specification<ReviewHead> specification, Pageable pageable);
    List<DashboardReviewByMonthDTO> findThisYearsReviewsGroupByMonth();
    List<DashboardUserRatingsPerCategoryDTO> findUserRatingsPerCategory(Long userId);
    List<DashboardUserBestRatedProductsDTO> findUserBestRatedProducts(Long userId);
    Double findUserDomesticProductPercentage(Long id);
    List<DashboardFavBrandProdDistDTO> findFavBrandProdDist(Long userId);
}

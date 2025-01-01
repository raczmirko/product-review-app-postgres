package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.DashboardFavBrandProdDistDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserRatingsPerCategoryDTO;
import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import hu.okrim.productreviewappcomplete.repository.ReviewHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewHeadServiceImpl implements ReviewHeadService{
    @Autowired
    ReviewHeadRepository reviewHeadRepository;
    @Override
    public ReviewHead findById(ReviewHeadId id) {
        return reviewHeadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.getProductId(), ReviewHead.class));
    }

    @Override
    public Optional<ReviewHead> findByUserAndProduct(User user, Product product) {
        return reviewHeadRepository.findByUserAndProduct(user, product);
    }

    @Override
    public void deleteById(ReviewHeadId id) {
        reviewHeadRepository.deleteById(id);
    }

    @Override
    public void save(ReviewHead reviewHead) {
        reviewHeadRepository.save(reviewHead);
    }

    @Override
    public List<ReviewHead> findAll() {
        return reviewHeadRepository.findAll();
    }

    @Override
    public Page<ReviewHead> findAllBySpecification(Specification<ReviewHead> specification, Pageable pageable) {
        return reviewHeadRepository.findAll(specification, pageable);
    }

    @Override
    public List<DashboardReviewByMonthDTO> findThisYearsReviewsGroupByMonth() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return reviewHeadRepository.findThisYearsReviewsGroupByMonth(currentYear);
    }

    @Override
    public List<DashboardUserRatingsPerCategoryDTO> findUserRatingsPerCategory(Long userId) {
        return reviewHeadRepository.findUserRatingsPerCategory(userId);
    }

    @Override
    public List<DashboardUserBestRatedProductsDTO> findUserBestRatedProducts(Long userId) {
        return reviewHeadRepository.findUserBestRatedProducts(userId);
    }

    @Override
    public Double findUserDomesticProductPercentage(Long id) {
        return reviewHeadRepository.findUserDomesticProductPercentage(id);
    }

    @Override
    public List<DashboardFavBrandProdDistDTO> findFavBrandProdDist(Long userId) {
        // Raw results are received as String-Double pairs
        // Example: Object['1-2', 83.33]
        List<Object[]> rawResults = reviewHeadRepository.findFavBrandProdDist(userId);
        List<DashboardFavBrandProdDistDTO> result = new ArrayList<>();

        for (Object[] rawResult : rawResults) {
            String range = (String) rawResult[0];
            BigDecimal percentageBigDecimal = (BigDecimal) rawResult[1];
            Double percentage = percentageBigDecimal.doubleValue();
            result.add(new DashboardFavBrandProdDistDTO(range, percentage));
        }

        return result;
    }
}

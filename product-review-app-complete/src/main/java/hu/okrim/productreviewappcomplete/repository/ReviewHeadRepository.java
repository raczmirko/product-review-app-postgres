package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.dto.*;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewHeadRepository extends JpaRepository<ReviewHead, ReviewHeadId> {
    Page<ReviewHead> findAll(Specification<ReviewHead> specification, Pageable pageable);

    Optional<ReviewHead> findByUserAndProduct(User user, Product product);

    @Query("SELECT new hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO(DATENAME(MONTH, r.date), COUNT(r)) " +
            "FROM ReviewHead r " +
            "WHERE DATEPART(YEAR, r.date) = :currentYear " +
            "GROUP BY DATENAME(MONTH, r.date) " +
            "ORDER BY DATENAME(MONTH, r.date) ASC")
    List<DashboardReviewByMonthDTO> findThisYearsReviewsGroupByMonth(@Param("currentYear") int currentYear);

    @Query("SELECT new hu.okrim.productreviewappcomplete.dto.DashboardUserRatingsPerCategoryDTO(" +
                "c, " +
                "ROUND((COUNT(1) * 1.0 / (SELECT COUNT(r2) FROM ReviewHead r2 WHERE r2.user.id = :userId)) * 100, 2), " +
                "COUNT(1)" +
            ") " +
            "FROM ReviewHead r " +
            "JOIN Product p ON r.product.id = p.id " +
            "JOIN Article a ON p.article.id = a.id " +
            "JOIN Category c ON a.category.id = c.id " +
            "WHERE r.user.id = :userId " +
            "GROUP BY c ")
    List<DashboardUserRatingsPerCategoryDTO> findUserRatingsPerCategory(@Param("userId") Long userId);

    @Query( "WITH helper_table AS (" +
            "SELECT " +
            "rh.product AS product, " +
            "ROUND(AVG(rh.valueForPrice) + COALESCE(AVG(rb.score), 0), 2) AS scoreAverage, " +
            "DENSE_RANK() OVER(ORDER BY AVG(rh.valueForPrice) + COALESCE(AVG(rb.score), 0) DESC) AS rank " +
            "FROM ReviewHead rh LEFT JOIN ReviewBody rb " +
            "ON rh.product.id = rb.id.productId AND rh.user.id = rb.id.userId " +
            "WHERE rh.user.id = :userId " +
            "GROUP BY rh.product) " +
        "SELECT new hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO(product, scoreAverage, rank) " +
        "FROM helper_table " +
        "WHERE rank <= 3 " +
        "ORDER BY scoreAverage DESC")
    List<DashboardUserBestRatedProductsDTO> findUserBestRatedProducts(@Param("userId") Long userId);

    @Query(
        "WITH domestic_review_count AS (" +
            "SELECT COUNT(1) AS domestic " +
            "FROM ReviewHead rh " +
            "INNER JOIN Product p ON rh.product.id = p.id " +
            "INNER JOIN Article a ON p.article.id = a.id " +
            "INNER JOIN User u ON rh.user.id = u.id " +
            "INNER JOIN Brand b ON a.brand.id = b.id " +
            "WHERE b.countryOfOrigin.countryCode = u.country.countryCode " +
            "AND rh.user.id = :userId" +
            "), " +
            "review_total AS (" +
            "SELECT COUNT(1) AS total " +
            "FROM ReviewHead " +
            "WHERE user.id = :userId" +
            ") " +
            "SELECT ROUND(domestic * 1.0 / total * 100, 2)" +
            "FROM domestic_review_count, review_total"
    )
    Double findUserDomesticProductPercentage(@Param("userId") Long userId);

//    @Query( "WITH reviews_per_brand AS ( " +
//                "SELECT a.brand.id AS brand, COUNT(1) as review_count " +
//                "FROM ReviewHead rh" +
//                "INNER JOIN Product p ON rh.product.id = p.id " +
//                "INNER JOIN Article a ON p.article.id = a.id " +
//                "GROUP BY a.brand.id " +
//            "), " +
//            "brands_ranked AS ( " +
//                "SELECT brand, " +
//                "RANK() OVER(ORDER BY review_count DESC) AS rank " +
//                "FROM reviews_per_brand " +
//            "), " +
//            "top_brands AS ( " +
//                "SELECT brand " +
//                "FROM brands_ranked " +
//                "WHERE rank = 1 " +
//            "), " +
//            "product_review_avg AS ( " +
//                "SELECT rh.product.id, a.brand.id, AVG(rh.valueForPrice) AS avg_head,  " +
//                "AVG(rb.score) AS avg_body,  " +
//                "CAST(ROUND((AVG(rh.value_for_price) + COALESCE(AVG(rb.score), 5)) / 2.0, 2) AS DECIMAL(10, 2)) AS avg_total " +
//                "FROM ReviewHead rh " +
//                "LEFT JOIN review_body rb ON rh.user = rb.user AND rh.product = rb.product " +
//                "INNER JOIN Product p ON rh.product.id = p.id " +
//                "INNER JOIN Article a ON p.article.id = a.id " +
//                "INNER JOIN top_brands tb ON a.brand.id = tb.brand  " +
//                "WHERE rh.user.id = :userId  " +
//                "AND a.brand.id IN (SELECT brand FROM top_brands) " +
//                "GROUP BY rh.product.id, a.brand.id " +
//            "), " +
//            "interval_categories AS ( " +
//                "SELECT  " +
//                "CASE  " +
//                "WHEN avg_total >= 1 AND avg_total < 2 THEN '1-2' " +
//                "WHEN avg_total >= 2 AND avg_total < 3 THEN '2-3' " +
//                "WHEN avg_total >= 3 AND avg_total < 4 THEN '3-4' " +
//                "WHEN avg_total >= 4 AND avg_total <= 5 THEN '4-5' " +
//                "END AS range " +
//                "FROM product_review_avg " +
//            ") " +
//            "SELECT new hu.okrim.productreviewappcomplete.dto.DashboardFavBrandProdDistDTO(range, " +
//            "CAST(ROUND(COUNT(1) * 100.0 / (SELECT COUNT(1) FROM product_review_avg), 2) AS DECIMAL(10,2)) AS percentage " +
//            "FROM interval_categories " +
//            "GROUP BY range")
    @Query(value = "WITH reviews_per_brand AS ( " +
                "SELECT a.brand AS brand, COUNT(1) as review_count " +
                "FROM review_head rh " +
                "INNER JOIN product p ON rh.product = p.id " +
                "INNER JOIN article a ON p.article = a.id " +
                "GROUP BY a.brand " +
            "), " +
            "brands_ranked AS ( " +
                "SELECT brand, " +
                "RANK() OVER(ORDER BY review_count DESC) AS rank " +
                "FROM reviews_per_brand " +
            "), " +
            "top_brands AS ( " +
                "SELECT brand " +
                "FROM brands_ranked " +
                "WHERE rank = 1 " +
            "), " +
            "product_review_avg AS ( " +
                "SELECT rh.product, a.brand, AVG(rh.value_for_price) AS avg_head,  " +
                "AVG(rb.score) AS avg_body,  " +
                "CAST(ROUND((AVG(rh.value_for_price) + COALESCE(AVG(rb.score), 5)) / 2.0, 2) AS DECIMAL(10, 2)) AS avg_total " +
                "FROM review_head rh " +
                "LEFT JOIN review_body rb ON rh.[user] = rb.[user] AND rh.product = rb.product " +
                "INNER JOIN product p ON rh.product = p.id " +
                "INNER JOIN article a ON p.article = a.id " +
                "INNER JOIN top_brands tb ON a.brand = tb.brand  " +
                "WHERE rh.[user] = :userId " +
                "AND a.brand IN (SELECT brand FROM top_brands) " +
                "GROUP BY rh.product, a.brand " +
            "), " +
            "interval_categories AS ( " +
                "SELECT  " +
                "CASE  " +
                "WHEN avg_total >= 1 AND avg_total < 2 THEN '1-2' " +
                "WHEN avg_total >= 2 AND avg_total < 3 THEN '2-3' " +
                "WHEN avg_total >= 3 AND avg_total < 4 THEN '3-4' " +
                "WHEN avg_total >= 4 AND avg_total <= 5 THEN '4-5' " +
                "END AS 'range' " +
                "FROM product_review_avg " +
            ") " +
            "SELECT [range], " +
            "CAST(ROUND(COUNT(1) * 100.0 / (SELECT COUNT(1) FROM product_review_avg), 2) AS DECIMAL(10,2)) AS percentage " +
            "FROM interval_categories " +
            "GROUP BY [range]", nativeQuery = true)
    List<Object[]> findFavBrandProdDist(@Param("userId") Long userId);
}

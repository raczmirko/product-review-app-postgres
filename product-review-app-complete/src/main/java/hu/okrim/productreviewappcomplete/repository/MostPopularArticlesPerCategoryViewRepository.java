package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MostPopularArticlesPerCategoryViewRepository extends JpaRepository<MostPopularArticlesPerCategoryView, String> {
}

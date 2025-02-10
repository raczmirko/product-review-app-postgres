package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerBrandView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MostPopularArticlesPerBrandViewRepository extends JpaRepository<MostPopularArticlesPerBrandView, String> {
}

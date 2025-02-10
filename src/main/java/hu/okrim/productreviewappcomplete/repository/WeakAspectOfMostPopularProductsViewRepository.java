package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.views.WeakAspectOfMostPopularProductsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WeakAspectOfMostPopularProductsViewRepository extends JpaRepository<WeakAspectOfMostPopularProductsView, UUID> {
}

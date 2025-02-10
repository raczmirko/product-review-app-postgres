package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.ReviewBody;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewBodyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewBodyRepository extends JpaRepository<ReviewBody, ReviewBodyId> {
}

package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}

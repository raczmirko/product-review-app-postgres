package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);
    @Query("SELECT p from Product p WHERE p.article.id = :articleId")
    List<Product> findByArticleId(@Param("articleId") Long articleId);
}

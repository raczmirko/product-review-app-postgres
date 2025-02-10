package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ArticleService {
    Article findById(Long id);
    void deleteById(Long id);
    void save (Article brand);
    List<Article> findAll();
    Page<Article> findAllBySpecification(Specification<Article> specification, Pageable pageable);
}

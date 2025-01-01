package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    ArticleRepository articleRepository;
    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Article.class));
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Page<Article> findAllBySpecification(Specification<Article> specification, Pageable pageable) {
        return articleRepository.findAll(specification, pageable);
    }
}

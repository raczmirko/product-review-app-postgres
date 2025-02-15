package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.ArticleDTO;
import hu.okrim.productreviewappcomplete.model.Article;

public class ArticleMapper {
    public static ArticleDTO mapToArticleDTO(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getName(),
                article.getBrand(),
                article.getCategory(),
                article.getDescription()
        );
    }

    public static Article mapToArticle(ArticleDTO articleDTO) {
        return new Article(
                articleDTO.getId() != null ? articleDTO.getId() : null,
                articleDTO.getName(),
                articleDTO.getBrand(),
                articleDTO.getCategory(),
                articleDTO.getDescription()
        );
    }
}

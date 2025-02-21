package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.ArticleDTO;
import hu.okrim.productreviewappcomplete.mapper.ArticleMapper;
import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.service.ArticleService;
import hu.okrim.productreviewappcomplete.specification.ArticleSpecificationBuilder;
import hu.okrim.productreviewappcomplete.util.SqlExceptionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @GetMapping("/all")
    public ResponseEntity<List<Article>> getArticles() {
        List<Article> articles = articleService.findAll();
        if (articles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable("id") Long id) {
        Article article = articleService.findById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteArticle(@PathVariable("id") Long id) {
        try {
            articleService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.articleDeleteErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deleteArticles(@PathVariable("ids") Long[] ids) {
        for (Long id : ids) {
            try {
                articleService.deleteById(id);
            } catch (Exception ex) {
                String errorMessage = SqlExceptionMessageHandler.articleDeleteErrorMessage(ex);
                return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> modifyArticle(@PathVariable("id") Long id, @RequestBody ArticleDTO articleDTO) {
        Article existingArticle = articleService.findById(id);

        if (existingArticle == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            existingArticle.setName(articleDTO.getName());
            existingArticle.setBrand(articleDTO.getBrand());
            existingArticle.setCategory(articleDTO.getCategory());
            existingArticle.setDescription(articleDTO.getDescription());
            articleService.save(existingArticle);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.articleUpdateErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO articleDTO) {
        Article article = ArticleMapper.mapToArticle(articleDTO);
        try {
            articleService.save(article);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Article>> searchArticles(@RequestParam(value = "searchText", required = false) String searchText,
                                                        @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                        @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("pageNumber") Integer pageNumber,
                                                        @RequestParam("orderByColumn") String orderByColumn,
                                                        @RequestParam("orderByDirection") String orderByDirection) {

        ArticleSpecificationBuilder<Article> articleSpecificationBuilder = new ArticleSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> articleSpecificationBuilder.withId(searchText);
                case "name" -> articleSpecificationBuilder.withName(searchText);
                case "brand" -> articleSpecificationBuilder.withBrandName(searchText);
                case "category" -> articleSpecificationBuilder.withCategoryName(searchText);
                case "description" -> articleSpecificationBuilder.withDescription(searchText);
                default -> {

                }
            }
        } else {
            if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
                // When searchColumn is not provided all fields are searched
                articleSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Article> specification = articleSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Article> articlePage = articleService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(articlePage, HttpStatus.OK);
    }
}

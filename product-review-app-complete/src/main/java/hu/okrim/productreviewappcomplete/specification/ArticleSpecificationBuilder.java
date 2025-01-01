package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.model.Country;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArticleSpecificationBuilder<Article> {
    private final List<Specification<Article>> specifications;

    public ArticleSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public ArticleSpecificationBuilder<Article> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        }
        catch (NumberFormatException e) {
            return this;
        }
    }

    public ArticleSpecificationBuilder<Article> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public ArticleSpecificationBuilder<Article> withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }

    public ArticleSpecificationBuilder<Article> withCategoryName(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Article, Category> categoryJoin = root.join("category");
                return builder.like(categoryJoin.get("name"), "%" + categoryName + "%");
            });
        }
        return this;
    }

    public ArticleSpecificationBuilder<Article> withBrandName(String brandName) {
        if (brandName != null && !brandName.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Article, Brand> brandJoin = root.join("brand");
                return builder.like(brandJoin.get("name"), "%" + brandName + "%");
            });
        }
        return this;
    }

    // Add OR criteria for quickFilterValues
    public ArticleSpecificationBuilder<Article> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<Article>> orSpecifications = new ArrayList<>();
            for (String value : quickFilterValues) {
                try {
                    int intValue = Integer.parseInt(value);
                    orSpecifications.add((root, query, builder) ->
                            builder.or(
                                    builder.equal(root.get("id"), intValue)
                            )
                    );
                } catch (NumberFormatException e) {
                    // The case where the value cannot be converted to an integer
                    // For example searching for 'OK', thus ID cannot be equal with 'OK'
                    // Nothing to be done, ID filter is simply ignored
                }
                orSpecifications.add((root, query, builder) ->
                        builder.or(
                                builder.like(root.get("name"), "%" + value + "%"),
                                builder.like(root.get("description"), "%" + value + "%"),
                                builder.like(root.join("brand").get("name"), "%" + value + "%"),
                                builder.like(root.join("category").get("name"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<Article> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Article> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

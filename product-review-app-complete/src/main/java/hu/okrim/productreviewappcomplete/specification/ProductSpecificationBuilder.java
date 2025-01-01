package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Packaging;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationBuilder<Product> {
    private final List<Specification<Product>> specifications;

    public ProductSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public ProductSpecificationBuilder<Product> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        }
        catch (NumberFormatException e) {
            return this;
        }
    }

    public ProductSpecificationBuilder<Product> withArticleName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Product, Article> articleJoin = root.join("article");
                return builder.like(articleJoin.get("name"), "%" + name + "%");
            });
        }
        return this;
    }

    public ProductSpecificationBuilder<Product> withPackagingName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Product, Packaging> packagingJoin = root.join("packaging");
                return builder.like(packagingJoin.get("name"), "%" + name + "%");
            });
        }
        return this;
    }

    public ProductSpecificationBuilder<Product> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<Product>> orSpecifications = new ArrayList<>();
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
                                builder.like(root.join("article").get("name"), "%" + value + "%"),
                                builder.like(root.join("packaging").get("name"), "%" + value + "%"),
                                builder.like(root.join("article").get("category").get("name"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<Product> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Product> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

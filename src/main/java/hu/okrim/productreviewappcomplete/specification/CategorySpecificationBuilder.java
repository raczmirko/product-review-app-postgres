package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Country;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CategorySpecificationBuilder<Category> {
    private final List<Specification<Category>> specifications;

    public CategorySpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public CategorySpecificationBuilder<Category> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        } catch (NumberFormatException e) {
            return this;
        }
    }

    public CategorySpecificationBuilder<Category> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public CategorySpecificationBuilder<Category> withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }

    public CategorySpecificationBuilder<Category> withParentCategory(String parentCategory) {
        if (parentCategory != null && !parentCategory.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Category, Country> countryJoin = root.join("parentCategory");
                return builder.like(countryJoin.get("name"), "%" + parentCategory + "%");
            });
        }
        return this;
    }

    // Add OR criteria for quickFilterValues
    public CategorySpecificationBuilder<Category> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<Category>> orSpecifications = new ArrayList<>();
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
                                builder.like(root.join("parentCategory", JoinType.LEFT).get("name"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<Category> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Category> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

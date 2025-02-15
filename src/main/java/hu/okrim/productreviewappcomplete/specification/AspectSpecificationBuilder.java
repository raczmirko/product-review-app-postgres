package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Category;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AspectSpecificationBuilder<Aspect> {
    private final List<Specification<Aspect>> specifications;

    public AspectSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public AspectSpecificationBuilder<Aspect> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        } catch (NumberFormatException e) {
            return this;
        }
    }

    public AspectSpecificationBuilder<Aspect> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public AspectSpecificationBuilder<Aspect> withQuestion(String question) {
        if (question != null && !question.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("question"), "%" + question + "%"));
        }
        return this;
    }

    public AspectSpecificationBuilder<Aspect> withCategoryName(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<Aspect, Category> categoryJoin = root.join("category");
                return builder.like(categoryJoin.get("name"), "%" + categoryName + "%");
            });
        }
        return this;
    }

    // Add OR criteria for quickFilterValues
    public AspectSpecificationBuilder<Aspect> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<Aspect>> orSpecifications = new ArrayList<>();
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
                                builder.like(root.get("question"), "%" + value + "%"),
                                builder.like(root.join("category").get("name"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<Aspect> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Aspect> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

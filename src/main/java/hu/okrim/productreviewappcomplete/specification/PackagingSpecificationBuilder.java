package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Category;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PackagingSpecificationBuilder<Packaging> {
    private final List<Specification<Packaging>> specifications;

    public PackagingSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public PackagingSpecificationBuilder<Packaging> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        }
        catch (NumberFormatException e) {
            return this;
        }
    }

    public PackagingSpecificationBuilder<Packaging> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public PackagingSpecificationBuilder<Packaging> withUnitOfMeasureName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("unitOfMeasureName"), "%" + name + "%"));
        }
        return this;
    }

    public PackagingSpecificationBuilder<Packaging> withUnitOfMeasure(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("unitOfMeasure"), "%" + name + "%"));
        }
        return this;
    }

    public PackagingSpecificationBuilder<Packaging> withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }

    public PackagingSpecificationBuilder<Packaging> withSize(String size) {
        if (size != null && !size.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("size"), "%" + size + "%"));
        }
        return this;
    }

    public PackagingSpecificationBuilder<Packaging> withAmount(Short amount) {
        if (amount != null) {
            specifications.add((root, query, builder) -> builder.equal(root.get("amount"), amount));
        }
        return this;
    }

    // Add OR criteria for quickFilterValues
    public PackagingSpecificationBuilder<Packaging> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<Packaging>> orSpecifications = new ArrayList<>();
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
                                builder.equal(root.get("amount"), value),
                                builder.like(root.get("size"), "%" + value + "%"),
                                builder.like(root.get("unitOfMeasure"), "%" + value + "%"),
                                builder.like(root.get("unitOfMeasureName"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<Packaging> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Packaging> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

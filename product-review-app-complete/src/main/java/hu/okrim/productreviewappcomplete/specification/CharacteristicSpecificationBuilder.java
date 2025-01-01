package hu.okrim.productreviewappcomplete.specification;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.model.Characteristic;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CharacteristicSpecificationBuilder<Characteristic> {
    private final List<Specification<Characteristic>> specifications;

    public CharacteristicSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public CharacteristicSpecificationBuilder<Characteristic> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        }
        catch (NumberFormatException e) {
            return this;
        }
    }

    public CharacteristicSpecificationBuilder<Characteristic> withName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }
        return this;
    }

    public CharacteristicSpecificationBuilder<Characteristic> withUnitOfMeasureName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("unitOfMeasureName"), "%" + name + "%"));
        }
        return this;
    }

    public CharacteristicSpecificationBuilder<Characteristic> withUnitOfMeasure(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("unitOfMeasure"), "%" + name + "%"));
        }
        return this;
    }

    public CharacteristicSpecificationBuilder<Characteristic> withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }

    public CharacteristicSpecificationBuilder<Characteristic> withCategoryName(String categoryName) {
        if (categoryName != null && !categoryName.isEmpty()) {
            specifications.add((root, query, builder) -> {
                // Joining Characteristic with Category
                Join<Characteristic, Category> categoryJoin = root.join("categories", JoinType.INNER);
                return builder.like(categoryJoin.get("name"), "%" + categoryName + "%");
            });
        }
        return this;
    }

    // Add OR criteria for quickFilterValues
    public CharacteristicSpecificationBuilder<Characteristic> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<Characteristic>> orSpecifications = new ArrayList<>();
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
                                builder.like(root.get("unitOfMeasureName"), "%" + value + "%"),
                                builder.like(root.get("unitOfMeasure"), "%" + value + "%"),
                                builder.like(root.get("description"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<Characteristic> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<Characteristic> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

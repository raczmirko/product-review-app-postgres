package hu.okrim.productreviewappcomplete.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationBuilder<User> {
    private final List<Specification<User>> specifications;

    public UserSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public UserSpecificationBuilder<User> withId(String id) {
        try {
            int numericId = Integer.parseInt(id);
            specifications.add((root, query, builder) -> builder.equal(root.get("id"), numericId));
            return this;
        } catch (NumberFormatException e) {
            return this;
        }
    }

    public UserSpecificationBuilder<User> withUsername(String username) {
        if (username != null && !username.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("username"), "%" + username + "%"));
        }
        return this;
    }

    public UserSpecificationBuilder<User> withIsActive(String isActive) {
        boolean trueOrFalse = Boolean.parseBoolean(isActive);
        specifications.add((root, query, builder) -> builder.equal(root.get("active"), trueOrFalse));
        return this;
    }

    // Add OR criteria for quickFilterValues
    public UserSpecificationBuilder<User> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<User>> orSpecifications = new ArrayList<>();
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
                                builder.equal(root.get("active"), Boolean.parseBoolean(value))
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<User> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<User> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

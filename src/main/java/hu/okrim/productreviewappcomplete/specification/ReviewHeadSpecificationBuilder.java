package hu.okrim.productreviewappcomplete.specification;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReviewHeadSpecificationBuilder<ReviewHead> {
    private final List<Specification<ReviewHead>> specifications;

    public ReviewHeadSpecificationBuilder() {
        this.specifications = new ArrayList<>();
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withProductId(String productId) {
        try {
            int numericId = Integer.parseInt(productId);
            specifications.add((root, query, builder) -> builder.equal(root.get("productId"), numericId));
            return this;
        } catch (NumberFormatException e) {
            return this;
        }
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withUserId(String userId) {
        try {
            int numericId = Integer.parseInt(userId);
            specifications.add((root, query, builder) -> builder.equal(root.get("userId"), numericId));
            return this;
        } catch (NumberFormatException e) {
            return this;
        }
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withUsername(String username) {
        if (username != null && !username.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<ReviewHead, User> userJoin = root.join("user");
                return builder.like(userJoin.get("username"), "%" + username + "%");
            });
        }
        return this;
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withDate(String date) {
        try {
            Instant parsedDate = Instant.parse(date);
            specifications.add((root, query, builder) -> builder.equal(root.get("date"), parsedDate));
        } catch (DateTimeParseException e) {
            // Handle invalid date format if needed
        }
        return this;
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withArticleName(String name) {
        if (name != null && !name.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<ReviewHead, Product> productJoin = root.join("product");
                Join<Product, Article> articleJoin = productJoin.join("article");
                return builder.like(articleJoin.get("name"), "%" + name + "%");
            });
        }
        return this;
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withDescription(String description) {
        if (description != null && !description.isEmpty()) {
            specifications.add((root, query, builder) -> builder.like(root.get("description"), "%" + description + "%"));
        }
        return this;
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withRecommended(String recommended) {
        boolean trueOrFalse = Boolean.parseBoolean(recommended);
        specifications.add((root, query, builder) -> builder.equal(root.get("recommended"), trueOrFalse));
        return this;
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withPurchaseCountryName(String countryName) {
        if (countryName != null && !countryName.isEmpty()) {
            specifications.add((root, query, builder) -> {
                Join<ReviewHead, Country> countryJoin = root.join("purchaseCountry");
                return builder.like(countryJoin.get("name"), "%" + countryName + "%");
            });
        }
        return this;
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withValueForPrice(String valueForPrice) {
        try {
            int numericId = Integer.parseInt(valueForPrice);
            specifications.add((root, query, builder) -> builder.equal(root.get("valueForPrice"), numericId));
            return this;
        } catch (NumberFormatException e) {
            return this;
        }
    }

    public ReviewHeadSpecificationBuilder<ReviewHead> withQuickFilterValues(List<String> quickFilterValues) {
        if (quickFilterValues != null && !quickFilterValues.isEmpty()) {
            List<Specification<ReviewHead>> orSpecifications = new ArrayList<>();
            for (String value : quickFilterValues) {
                try {
                    int intValue = Integer.parseInt(value);
                    orSpecifications.add((root, query, builder) ->
                            builder.or(
                                    builder.equal(root.get("valueForPrice"), intValue)
                            )
                    );
                } catch (NumberFormatException ignored) {
                    // The case where the value cannot be converted to an integer
                    // For example searching for 'OK', thus ID cannot be equal with 'OK'
                    // Nothing to be done, ID filter is simply ignored
                }
                try {
                    Instant parsedDate = Instant.parse(value);
                    orSpecifications.add((root, query, builder) ->
                            builder.or(
                                    builder.equal(root.get("date"), parsedDate)
                            )
                    );
                } catch (DateTimeParseException ignored) {
                }
                orSpecifications.add((root, query, builder) ->
                        builder.or(
                                builder.like(root.join("user").get("username"), "%" + value + "%"),
                                builder.like(root.join("product").join("article").get("name"), "%" + value + "%"),
                                builder.like(root.get("description"), "%" + value + "%"),
                                builder.equal(root.get("recommended"), Boolean.parseBoolean(value)),
                                builder.like(root.join("purchaseCountry").get("name"), "%" + value + "%")
                        ));
            }
            specifications.add(Specification.where(orSpecifications.stream().reduce((a, b) -> a.or(b)).orElse(null)));
        }
        return this;
    }

    public Specification<ReviewHead> build() {
        if (specifications.isEmpty()) {
            return null; // No criteria specified
        }
        Specification<ReviewHead> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }
}

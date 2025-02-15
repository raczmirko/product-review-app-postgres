package hu.okrim.productreviewappcomplete.model.compositeKey;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReviewHeadId implements Serializable {
    private Long userId;
    private Long productId;

    public ReviewHeadId() {
    }

    public ReviewHeadId(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewHeadId that = (ReviewHeadId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId);
    }
}

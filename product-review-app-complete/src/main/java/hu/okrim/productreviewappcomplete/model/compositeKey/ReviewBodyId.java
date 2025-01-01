package hu.okrim.productreviewappcomplete.model.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReviewBodyId implements Serializable {
    @Column(name = "[user]")
    private Long userId;
    @Column(name = "product")
    private Long productId;
    @Column(name = "aspect")
    private Long aspectId;

    public ReviewBodyId() {
    }

    public ReviewBodyId(Long userId, Long productId, Long aspectId) {
        this.userId = userId;
        this.productId = productId;
        this.aspectId = aspectId;
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

    public Long getAspectId() {
        return aspectId;
    }

    public void setAspectId(Long aspectId) {
        this.aspectId = aspectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewBodyId that = (ReviewBodyId) o;
        return Objects.equals(productId, that.productId)
                && Objects.equals(userId, that.userId)
                && Objects.equals(aspectId, that.aspectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userId, aspectId);
    }
}

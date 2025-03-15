package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import hu.okrim.productreviewappcomplete.util.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review_head")
@EntityListeners(AuditListener.class)
public class ReviewHead {
    @EmbeddedId
    private ReviewHeadId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "[user]")
    private User user;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product")
    private Product product;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(name = "overall_review", nullable = false, length = 1000)
    private String description;
    @Column(nullable = false)
    private Boolean recommended;
    @ManyToOne
    @JoinColumn(name = "purchase_country", nullable = false)
    private Country purchaseCountry;
    @Column(nullable = false)
    private Short valueForPrice;
    @OneToMany(mappedBy = "reviewHead", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewBody> reviewBodyItems;

    public ReviewHead(ReviewHeadId id, User user,
                      Product product, LocalDateTime date,
                      String description, Boolean recommended,
                      Country purchaseCountry, Short valueForPrice) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.date = date;
        this.description = description;
        this.recommended = recommended;
        this.purchaseCountry = purchaseCountry;
        this.valueForPrice = valueForPrice;
    }
}

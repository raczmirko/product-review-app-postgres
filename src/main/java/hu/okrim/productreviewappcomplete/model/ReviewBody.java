package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewBodyId;
import hu.okrim.productreviewappcomplete.util.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "review_body")
@EntityListeners(AuditListener.class)
public class ReviewBody {
    @EmbeddedId
    private ReviewBodyId id;

    @Column(name = "score", nullable = false)
    private Short score;

    @JsonIgnore
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "[user]", referencedColumnName = "user", insertable = false, updatable = false),
            @JoinColumn(name = "product", referencedColumnName = "product", insertable = false, updatable = false)
    })
    private ReviewHead reviewHead;
}

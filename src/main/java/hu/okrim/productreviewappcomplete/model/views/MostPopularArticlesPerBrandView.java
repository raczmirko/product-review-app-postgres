package hu.okrim.productreviewappcomplete.model.views;

import hu.okrim.productreviewappcomplete.model.compositeKey.MostPopularArticlesPerBrandViewId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "v_most_popular_articles_of_brands")
public class MostPopularArticlesPerBrandView {

    @EmbeddedId
    private MostPopularArticlesPerBrandViewId id;

    private Double average;
}

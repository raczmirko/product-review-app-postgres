package hu.okrim.productreviewappcomplete.model.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularArticlesPerBrandViewId implements Serializable {
    private String brand;
    private String article;
}


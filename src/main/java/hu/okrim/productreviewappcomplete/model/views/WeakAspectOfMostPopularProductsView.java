package hu.okrim.productreviewappcomplete.model.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "v_weakness_of_most_popular_products")
public class WeakAspectOfMostPopularProductsView {
    @Id
    @JsonIgnore
    private UUID id;
    private Long productId;
    private Long articleId;
    private String article;
    private Long packagingId;
    private String packaging;
    private Long aspectId;
    private String weakestAspect;
    private String average;
}

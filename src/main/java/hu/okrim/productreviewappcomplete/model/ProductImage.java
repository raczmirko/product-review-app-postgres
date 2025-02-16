package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;

    public ProductImage(Product product, byte[] image) {
        this.product = product;
        this.image = image;
    }
}


package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
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

    @Lob
    @Column(name = "image", columnDefinition = "varbinary(max)")
    private byte[] image;

    public ProductImage(Product product, byte[] image){
        this.product = product;
        this.image = image;
    }
}


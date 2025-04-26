package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.audit.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
@EntityListeners(AuditListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "packaging", nullable = false)
    private Packaging packaging;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCharacteristicValue> productCharacteristicValues;

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", article=" + String.format("%s (id:%d)", article.getName(), article.getId()) +
                ", packaging=" + String.format("%s (id:%d)", packaging.getName(), packaging.getId()) +
                ", productCharacteristicValues=[" +
                (productCharacteristicValues == null ? "null" :
                        productCharacteristicValues.stream()
                                .map(val -> String.format("%s-%s", val.getCharacteristic().getName(), val.getValue()))
                                .collect(Collectors.joining(", "))
                ) +
                "]" +
                '}';
    }
}

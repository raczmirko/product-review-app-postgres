package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.okrim.productreviewappcomplete.audit.AuditListener;
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
@Table(name = "product_characteristic_value")
@EntityListeners(AuditListener.class)
public class ProductCharacteristicValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "characteristic", nullable = false)
    private Characteristic characteristic;

    @Column(name = "value", nullable = false, length = 100)
    private String value;

    @Override
    public String toString() {
        return "ProductCharacteristicValue{" +
                "id=" + id +
                ", product=" + String.format("%s (%d)", product.getArticle().getName(), product.getId()) +
                ", characteristic=" + String.format("%s (%d)", characteristic.getName(), characteristic.getId()) +
                ", value='" + value + '\'' +
                '}';
    }
}

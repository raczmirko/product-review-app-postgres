package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.okrim.productreviewappcomplete.util.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

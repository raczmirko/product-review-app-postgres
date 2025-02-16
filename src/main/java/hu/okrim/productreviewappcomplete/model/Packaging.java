package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.util.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "packaging")
@EntityListeners(AuditListener.class)
public class Packaging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Short amount;
    @Column(length = 100)
    private String unitOfMeasureName;
    @Column(length = 100)
    private String unitOfMeasure;
    @Column(length = 100)
    private String size;
}

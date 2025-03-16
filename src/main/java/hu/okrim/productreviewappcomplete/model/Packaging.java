package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.audit.AuditListener;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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

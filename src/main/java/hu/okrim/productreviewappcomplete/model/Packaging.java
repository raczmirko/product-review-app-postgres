package hu.okrim.productreviewappcomplete.model;

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

    @Override
    public String toString() {
        return "Packaging{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", unitOfMeasure=" + String.format("%s (%s)", unitOfMeasureName, unitOfMeasure) +
                ", size='" + size + '\'' +
                '}';
    }
}

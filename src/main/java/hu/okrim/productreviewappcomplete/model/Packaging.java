package hu.okrim.productreviewappcomplete.model;

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
@Table(name = "packaging")
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

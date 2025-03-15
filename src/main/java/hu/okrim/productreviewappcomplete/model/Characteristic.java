package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.okrim.productreviewappcomplete.util.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "characteristic")
@EntityListeners(AuditListener.class)
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EqualsAndHashCode.Include
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    /* Unit of measure and its name must be both present or both null.
    This is handled on the database level with a CHECK constraint. */
    @Column(length = 100)
    private String unitOfMeasureName;
    @Column(length = 100)
    private String unitOfMeasure;
    @Column(length = 100)
    private String description;
    @JsonIgnore
    @ManyToMany(mappedBy = "characteristics")
    private Set<Category> categories;
}

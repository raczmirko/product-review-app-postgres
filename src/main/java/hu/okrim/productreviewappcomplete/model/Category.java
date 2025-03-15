package hu.okrim.productreviewappcomplete.model;

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
@Entity
@Table(name = "category")
@EntityListeners(AuditListener.class)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_category")
    private Category parentCategory;
    @Column(length = 1000)
    private String description;
    @ManyToMany
    @JoinTable(
            name = "category_x_characteristic",
            joinColumns = @JoinColumn(name = "category"),
            inverseJoinColumns = @JoinColumn(name = "characteristic")
    )
    private Set<Characteristic> characteristics;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

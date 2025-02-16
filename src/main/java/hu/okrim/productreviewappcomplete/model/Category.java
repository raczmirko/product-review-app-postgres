package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.util.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public Category(Long id, String name, Category parentCategory, String description) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.description = description;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

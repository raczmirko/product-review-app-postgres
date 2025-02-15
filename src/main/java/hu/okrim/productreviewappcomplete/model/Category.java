package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
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

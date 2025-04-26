package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.audit.AuditListener;
import hu.okrim.productreviewappcomplete.util.ToStringHelper;
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
@Table(name = "article")
@EntityListeners(AuditListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @Column(length = 1000)
    private String description;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand=" + String.format("%s (id:%d)", brand.getName(), brand.getId()) +
                ", category=" + String.format("%s (id:%d)", category.getName(), category.getId()) +
                ", description='" + ToStringHelper.safe(description) + '\'' +
                '}';
    }
}

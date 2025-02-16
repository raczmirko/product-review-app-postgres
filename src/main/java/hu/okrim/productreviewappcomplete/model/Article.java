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
}

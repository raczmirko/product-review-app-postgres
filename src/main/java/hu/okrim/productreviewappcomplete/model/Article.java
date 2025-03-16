package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.audit.AuditListener;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
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

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
@Table(name = "aspect")
@EntityListeners(AuditListener.class)
public class Aspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String question;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @Override
    public String toString() {
        return "Aspect{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", question='" + question + '\'' +
                ", category=" + String.format("%s (id:%d)", category.getName(), category.getId()) +
                '}';
    }
}

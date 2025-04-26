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
@Table(name = "brand")
@EntityListeners(AuditListener.class)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country countryOfOrigin;

    @Column(length = 1000)
    private String description;

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countryOfOrigin=" + String.format("%s (id:%s)", countryOfOrigin.getName(), countryOfOrigin.getCountryCode()) +
                ", description='" + ToStringHelper.safe(description) + '\'' +
                '}';
    }
}

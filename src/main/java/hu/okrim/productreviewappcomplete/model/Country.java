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
@Table(name = "country")
@EntityListeners(AuditListener.class)
public class Country {
    @Id
    @Column(length = 3)
    private String countryCode;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
}

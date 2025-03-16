package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.audit.AuditListener;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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

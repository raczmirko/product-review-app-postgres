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
@Table(name = "role")
@EntityListeners(AuditListener.class)
public class Role {
    @Id
    @Column(length = 100)
    private String name;

    public Role(RoleType roleType) {
        this.setName(roleType.name());
    }
}

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

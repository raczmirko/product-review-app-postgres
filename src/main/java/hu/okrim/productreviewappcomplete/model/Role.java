package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.util.AuditListener;
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

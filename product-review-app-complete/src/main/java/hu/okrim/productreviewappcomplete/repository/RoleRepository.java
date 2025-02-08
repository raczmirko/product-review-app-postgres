package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
}

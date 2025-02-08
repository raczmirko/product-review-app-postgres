package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Role;

public interface RoleService {
    Role findByName(String name);
    Role getUserRole();
}

package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Role;
import hu.okrim.productreviewappcomplete.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role getUserRole() {
        return roleRepository.findByName("user");
    }
}

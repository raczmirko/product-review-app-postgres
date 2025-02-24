package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
    User findById(Long id);

    User findByUsername(String username);

    void save(User user);

    void deleteById(Long id);

    List<User> findAll();

    String getUserRole(String username);

    List<DashboardMostActiveUserDTO> findMostActiveUsers(Pageable pageable);

    Page<User> findAllBySpecification(Specification<User> specification, Pageable pageable);
}

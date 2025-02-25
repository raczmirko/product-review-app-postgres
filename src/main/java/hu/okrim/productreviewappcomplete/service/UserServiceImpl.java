package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    static User unwrapUser(Optional<User> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(id, User.class);
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, id);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user, 404L);
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public String getUserRole(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() ? user.get().getRole().getName() : "error";
    }

    @Override
    public List<DashboardMostActiveUserDTO> findMostActiveUsers(Pageable pageable) {
        return userRepository.findMostActiveUsers(pageable);
    }

    @Override
    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public void enableDisableUser(String username, Boolean isActive) {
        Optional<User> user = userRepository.findByUsername(username);
        User userEntity = unwrapUser(user, 404L);
        userEntity.setIsActive(isActive);
        userRepository.save(userEntity);
    }
}

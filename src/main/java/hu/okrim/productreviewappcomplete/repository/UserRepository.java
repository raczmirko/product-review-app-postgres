package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByUsername(String username);

    @Query("SELECT new hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO(u, COUNT(r)) " +
            "FROM User u JOIN ReviewHead r ON r.user.id = u.id " +
            "GROUP BY u " +
            "ORDER BY COUNT(r) DESC")
    List<DashboardMostActiveUserDTO> findMostActiveUsers(Pageable pageable);
}

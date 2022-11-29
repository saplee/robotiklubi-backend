package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

    List<User> findAllByRole(Integer roleId);

    Optional<User> findByEmailIgnoreCase(String email);
}

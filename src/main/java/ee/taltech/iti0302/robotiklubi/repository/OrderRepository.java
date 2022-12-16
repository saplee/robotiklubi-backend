package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface OrderRepository extends JpaRepositoryImplementation<Order, Long> {
    List<Order> findAllByClientId(Long id);

    List<Order> findAllBySliced(Boolean sliced);
}

package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface WikiTagRepository extends JpaRepositoryImplementation<WikiTag, Long> {

    Optional<WikiTag> findByTagIgnoreCase(String tag);
}

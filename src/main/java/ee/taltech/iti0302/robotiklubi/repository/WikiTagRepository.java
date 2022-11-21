package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Collection;
import java.util.List;

public interface WikiTagRepository extends JpaRepositoryImplementation<WikiTag, Long> {

    List<WikiTag> findAllByIdIn(Collection<Long> ids);
}

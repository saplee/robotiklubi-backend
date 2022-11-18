package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface WikiTagRelationRepository extends JpaRepositoryImplementation<WikiTagRelation, Long> {

    List<WikiTagRelation> findAllByPageId(Integer id);
}

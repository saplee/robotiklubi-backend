package ee.taltech.iti0302.robotiklubi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WikiTagRelationRepository extends JpaRepositoryImplementation<WikiTagRelation, Long> {

    List<WikiTagRelation> findAllByPageId(Long pageId);

    @Query(value = "SELECT page_id FROM wiki_tag_relations WHERE tag_id IN (:#{#tagIds}) GROUP BY page_id HAVING count(tag_id) = :#{#count}", nativeQuery = true)
    List<Integer> getPagesWithAllMatchingTags(@Param("tagIds") List<Integer> tagIds, @Param("count") int count);

    Optional<WikiTagRelation> findWikiTagRelationByTagAndPage(WikiTag tag, WikiPage page);

    List<WikiTagRelation> findAllByTag(WikiTag tag);

    List<WikiTagRelation> findAllByPage(WikiPage page);
}

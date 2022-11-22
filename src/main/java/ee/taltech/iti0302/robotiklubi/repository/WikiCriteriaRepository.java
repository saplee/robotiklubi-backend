package ee.taltech.iti0302.robotiklubi.repository;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WikiCriteriaRepository {

    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String CREATION_DATE = "createdAt";
    private static final String EDITING_DATE = "lastEdited";

    private final EntityManager entityManager;

    public Map.Entry<Long, List<WikiPage>> findAllByCriteria(WikiSearchCriteria searchCriteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<WikiPage> pageQuery = cb.createQuery(WikiPage.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<WikiPage> root = pageQuery.from(WikiPage.class);
        // Create search criteria
        List<Predicate> predicates = createCriteria(searchCriteria, cb, root);
        // Apply search criteria
        pageQuery.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        countQuery.select(cb.count(countQuery.from(WikiPage.class))).where(cb.and(predicates.toArray(new Predicate[0])));
        // Sorting
        applySorting(searchCriteria, cb, pageQuery, root);
        // Pagination
        TypedQuery<WikiPage> pageManagedQuery = entityManager.createQuery(pageQuery);
        if (searchCriteria.getFirstResult() != null) pageManagedQuery.setFirstResult(searchCriteria.getFirstResult());
        pageManagedQuery.setMaxResults(searchCriteria.getResultsPerPage());
        // Find results
        return Map.entry(
                entityManager.createQuery(countQuery).getSingleResult(),
                pageManagedQuery.getResultList());
    }

    private List<Predicate> createCriteria(WikiSearchCriteria searchCriteria, CriteriaBuilder cb, Root<WikiPage> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (searchCriteria.getTitleSearch() != null && !searchCriteria.getTitleSearch().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get(TITLE)), "%" + searchCriteria.getTitleSearch().toLowerCase() + "%"));
        }
        if (searchCriteria.getContentSearch() != null && !searchCriteria.getContentSearch().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get(CONTENT)), "%" + searchCriteria.getContentSearch().toLowerCase() + "%"));
        }
        return predicates;
    }

    private void applySorting(WikiSearchCriteria searchCriteria, CriteriaBuilder cb, CriteriaQuery<WikiPage> query, Root<WikiPage> root) {
        if (searchCriteria.isSortByTitle()) {
            if (searchCriteria.isSortAscending()) query.orderBy(cb.asc(root.get(TITLE)));
            else query.orderBy(cb.desc(root.get(TITLE)));
        }
        else if (searchCriteria.isSortByCreationDate()) {
            if (searchCriteria.isSortAscending()) query.orderBy(cb.asc(root.get(CREATION_DATE)));
            else query.orderBy(cb.desc(root.get(CREATION_DATE)));
        }
        else if (searchCriteria.isSortByEditDate()) {
            if (searchCriteria.isSortAscending()) query.orderBy(cb.asc(root.get(EDITING_DATE)));
            else query.orderBy(cb.desc(root.get(EDITING_DATE)));
        }

    }
}

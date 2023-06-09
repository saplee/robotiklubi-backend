package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.*;
import ee.taltech.iti0302.robotiklubi.exception.InternalServerException;
import ee.taltech.iti0302.robotiklubi.exception.NotFoundException;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMetaDataMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiTagMapper;
import ee.taltech.iti0302.robotiklubi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class WikiService {

    private static final int MAX_PAGINATION = 50;
    private static final int DEFAULT_PAGINATION = 10;
    private static final String PAGE_NOT_FOUND = "Wiki page not found.";

    private final WikiRepository wikiRepository;
    private final WikiPageMapper wikiPageMapper;
    private final WikiPageMetaDataMapper wikiPageMetaDataMapper;

    private final WikiTagMapper wikiTagMapper;

    private final WikiTagRelationRepository wikiTagRelationRepository;

    private final WikiCriteriaRepository wikiCriteriaRepository;

    private final UserRepository userRepository;

    public Long createPage(WikiPageDto wikiPageDto, String idString) {
        try {
            Optional<User> author = userRepository.findById(Long.valueOf(idString));
            if (author.isEmpty()) throw new NotFoundException("Invalid author ID.");
            WikiPage page = WikiPage.builder()
                    .title(wikiPageDto.getTitle())
                    .content(wikiPageDto.getContent())
                    .author(author.get())
                    .lastEditedBy(author.get())
                    .build();
            wikiRepository.save(page);
            return page.getId();
        } catch (Exception e) {throw new InternalServerException("Could not create wiki page.", e);}
    }

    public WikiPageDto getPageById(Long id) {
        try {
            Optional<WikiPage> pageOptional = wikiRepository.findById(id);
            if (pageOptional.isPresent()) return wikiPageMapper.toDto(pageOptional.get());
            throw new NotFoundException(PAGE_NOT_FOUND);
        } catch (Exception e) {throw new InternalServerException("Could not retrieve wiki page (id " + id + ").", e);}
    }

    public void updatePage(Long id, WikiPageDto wikiPageDto, String idString) {
        try {
            Optional<User> editor = userRepository.findById(Long.valueOf(idString));
            if (editor.isEmpty()) throw new NotFoundException("Invalid editor ID.");
            Optional<WikiPage> pageOptional = wikiRepository.findById(id);
            if (pageOptional.isEmpty()) throw new NotFoundException(PAGE_NOT_FOUND);
            WikiPage page = pageOptional.get();
            page.setTitle(wikiPageDto.getTitle());
            page.setContent(wikiPageDto.getContent());
            page.setLastEditedBy(editor.get());
        } catch (Exception e) {throw new InternalServerException("Could not update wiki page (id " + id + ").", e);}
    }

    public void deletePage(Long id) {
        try {
            Optional<WikiPage> pageOptional = wikiRepository.findById(id);
            if (pageOptional.isEmpty()) throw new NotFoundException(PAGE_NOT_FOUND);
            List<WikiTagRelation> relations = wikiTagRelationRepository.findAllByPage(pageOptional.get());
            wikiTagRelationRepository.deleteAll(relations);
            wikiRepository.delete(pageOptional.get());
        } catch (Exception e) {throw new InternalServerException("Could not delete wiki page (id " + id + ").", e);}
    }

    public List<TagDto> getPageTags(Long id) {
        try {
            List<WikiTag> tags = wikiTagRelationRepository.findAllByPageId(id).stream().map(WikiTagRelation::getTag).toList();
            return wikiTagMapper.toDtoList(tags);
        } catch (Exception e) {throw new InternalServerException("Could not retrieve tags for wiki page (id " + id + ").", e);}
    }

    public WikiSearchResult findAllByCriteria(WikiSearchCriteria searchCriteria) {
        long start = System.currentTimeMillis();
        if (searchCriteria.getResultsPerPage() == null) searchCriteria.setResultsPerPage(DEFAULT_PAGINATION);
        if (searchCriteria.getFirstResult() == null ||searchCriteria.getFirstResult() < 0) searchCriteria.setFirstResult(0);
        searchCriteria.setResultsPerPage(Math.min(searchCriteria.getResultsPerPage(), MAX_PAGINATION));
        Map.Entry<Long, List<WikiPage>> searchResults = wikiCriteriaRepository.findAllByCriteria(searchCriteria);
        if (searchResults.getValue().isEmpty()) throw new NotFoundException("No matching pages found.");
        int prevPage = -1;
        int nextPage = -1;
        if (searchCriteria.getFirstResult() - searchCriteria.getResultsPerPage() > 0) prevPage = searchCriteria.getFirstResult() - searchCriteria.getResultsPerPage();
        else if (searchCriteria.getFirstResult() != 0) prevPage = 0;
        if (searchCriteria.getFirstResult() + searchCriteria.getResultsPerPage() < searchResults.getKey()) nextPage = searchCriteria.getFirstResult() + searchCriteria.getResultsPerPage();
        return new WikiSearchResult(searchResults.getKey(), prevPage, nextPage, wikiPageMetaDataMapper.toDtoList(searchResults.getValue()), System.currentTimeMillis() - start);
    }
}

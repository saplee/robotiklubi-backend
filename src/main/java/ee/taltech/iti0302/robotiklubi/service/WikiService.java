package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.*;
import ee.taltech.iti0302.robotiklubi.exception.ApplicationException;
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

    private final WikiRepository wikiRepository;
    private final WikiPageMapper wikiPageMapper;
    private final WikiPageMetaDataMapper wikiPageMetaDataMapper;

    private final WikiCriteriaRepository wikiCriteriaRepository;

    private final WikiTagRepository wikiTagRepository;
    private final WikiTagMapper wikiTagMapper;
    private final WikiTagRelationRepository wikiTagRelationRepository;

    public WikiPageDto getPageById(Long id) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        if (pageOptional.isPresent()) {
            WikiPage page = pageOptional.get();
            page.setTitle(page.getTitle().strip());
            return wikiPageMapper.toDto(pageOptional.get());
        }
        return WikiPageDto.builder().title("").content("").build();
    }

    public List<TagDto> getPageTags(Long id) {
        List<Long> tagIds = wikiTagRelationRepository.findAllByPageId(id.intValue()).stream().map(r -> Long.valueOf(r.getTagId())).toList();
        List<WikiTag> tags = wikiTagRepository.findAllByIdIn(tagIds);
        return wikiTagMapper.toDtoList(tags);
    }

    public List<WikiPageMetaDataDto> getPagesByTag(Long id) {
        List<Long> pageIds = wikiTagRelationRepository.findAllByTagId(id.intValue()).stream().map(r -> Long.valueOf(r.getPageId())).toList();
        List<WikiPage> pages = wikiRepository.findAllById(pageIds);
        return wikiPageMetaDataMapper.toDtoList(pages);
    }

    public void createPage(WikiPageDto wikiPageDto) {
        try {
            WikiPage page = WikiPage.builder()
                    .title(wikiPageDto.getTitle())
                    .content(wikiPageDto.getContent())
                    .authorId(wikiPageDto.getAuthor())
                    .build();
            wikiRepository.save(page);
        } catch (Exception e) {
            throw new ApplicationException("Could not create wiki page.");
        }
    }

    public void updatePage(Long id, WikiPageDto wikiPageDto) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        if (pageOptional.isEmpty()) throw new NotFoundException("Wiki page not found.");
        try {
            WikiPage page = pageOptional.get();
            page.setTitle(wikiPageDto.getTitle());
            page.setContent(wikiPageDto.getContent());
            page.setLastEditedBy(wikiPageDto.getLastEditedBy());
            wikiRepository.save(page);
        } catch (Exception e) {
            throw new ApplicationException("Could not update wiki page.");
        }
    }

    public void deletePage(Long id) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        pageOptional.ifPresentOrElse(
                wikiRepository::delete,
                () -> {throw new NotFoundException("Wiki page not found.");});
    }

    public WikiSearchResult findAllByCriteria(WikiSearchCriteria searchCriteria) {
        if (searchCriteria.getResultsPerPage() == null) searchCriteria.setResultsPerPage(DEFAULT_PAGINATION);
        searchCriteria.setResultsPerPage(Math.min(searchCriteria.getResultsPerPage(), MAX_PAGINATION));
        Map.Entry<Long, List<WikiPage>> searchResults = wikiCriteriaRepository.findAllByCriteria(searchCriteria);
        if (searchResults.getValue().isEmpty()) throw new NotFoundException("No matching pages found.");
        return new WikiSearchResult(searchResults.getKey(), wikiPageMetaDataMapper.toDtoList(searchResults.getValue()));
    }
}
